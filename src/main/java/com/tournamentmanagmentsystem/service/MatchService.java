package com.tournamentmanagmentsystem.service;

import org.springframework.lang.NonNull;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.dto.request.MatchResultRequest;
import com.tournamentmanagmentsystem.dto.response.MatchResponse;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import com.tournamentmanagmentsystem.service.bracket.BracketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.tournamentmanagmentsystem.exception.NotFoundException;
import com.tournamentmanagmentsystem.exception.BusinessRuleViolationException;
import com.tournamentmanagmentsystem.exception.InvalidStateTransitionException;
import java.util.HashMap;

/**
 * Service for match result management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {

    private final MatchRepository matchRepository;
    private final AuditService auditService;
    private final StandingService standingService;
    private final BracketService bracketService;
    private final ModelMapper modelMapper;

    @Transactional
    @NonNull
    public MatchResponse updateResult(@NonNull UUID matchId, @NonNull MatchResultRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found: " + matchId));

        if (match.getStatus() == MatchStatus.DISPUTED) {
            throw new InvalidStateTransitionException("Cannot update result. Match is currently DISPUTED.");
        }
        if (match.getStatus() == MatchStatus.COMPLETED || match.getStatus() == MatchStatus.WALKOVER) {
            throw new InvalidStateTransitionException("Cannot update result. Match is already finalized.");
        }

        applyResultToMatch(match, request);
        Match savedMatch = matchRepository.save(match);

        logResultUpdate(savedMatch, request);
        processStandingsUpdate(savedMatch);
        triggerAdvancementLogic(savedMatch);

        log.info("Match result updated: ID={}, winnerID={}", savedMatch.getId(), request.getWinnerId());

        return modelMapper.map(savedMatch, MatchResponse.class);
    }

    private void applyResultToMatch(@NonNull Match match, @NonNull MatchResultRequest request) {
        match.setScore(request.getScore());
        match.setWinnerId(request.getWinnerId());
        
        if (Boolean.TRUE.equals(request.getForfeit())) {
            match.setStatus(MatchStatus.WALKOVER);
            if (match.getScore() == null || match.getScore().isEmpty()) {
                match.setScore(Map.of("note", "Forfeit/No-show"));
            }
        } else {
            validateBestOfScore(match, request);
            match.setStatus(MatchStatus.FINISHED);
        }
    }

    private void validateBestOfScore(Match match, MatchResultRequest request) {
        if (match.getEvent() != null && match.getEvent().getConfig() != null) {
            Object bestOfObj = match.getEvent().getConfig().get("bestOf");
            if (bestOfObj != null && request.getScore() != null && request.getWinnerId() != null) {
                int bestOf;
                try {
                    bestOf = Integer.parseInt(bestOfObj.toString());
                } catch (NumberFormatException e) {
                    return; // Ignore invalid config gracefully
                }
                
                int requiredWins = (bestOf / 2) + 1;
                Object winnerScoreObj = request.getScore().get(request.getWinnerId().toString());
                
                if (winnerScoreObj != null) {
                   try {
                       int winnerScore = Integer.parseInt(winnerScoreObj.toString());
                       if (winnerScore < requiredWins) {
                           throw new BusinessRuleViolationException("Winner score " + winnerScore + " does not meet Best-Of " + bestOf + " requirement (" + requiredWins + " wins)");
                       }
                   } catch (NumberFormatException e) {
                       // Non-integer score, skip validation
                   }
                }
            }
        }
    }

    private void logResultUpdate(@NonNull Match match, @NonNull MatchResultRequest request) {
        if (match.getId() != null) {
            auditService.log("UPDATE_RESULT", "MATCH", match.getId(), Map.of(
                    "winnerId", request.getWinnerId() != null ? request.getWinnerId().toString() : "null",
                    "score", request.getScore() != null ? request.getScore() : Collections.emptyMap()));
        }
    }

    private void processStandingsUpdate(@NonNull Match match) {
        if (match.getWinnerId() == null) {
            return;
        }

        UUID winnerId = match.getWinnerId();
        Participant participantA = match.getParticipantA();
        Participant participantB = match.getParticipantB();

        if (participantA == null || participantB == null) {
            return;
        }

        boolean isAWinner = Objects.equals(winnerId, participantA.getId());
        Participant winner = isAWinner ? participantA : participantB;
        Participant loser = isAWinner ? participantB : participantA;

        int winnerScore = extractScore(match, winner.getId());
        int loserScore = extractScore(match, loser.getId());
        int pointsDiff = winnerScore - loserScore;

        standingService.updateStanding(match.getEvent(), winner, 3, Map.of(
            "wins", 1, 
            "pointsDiff", pointsDiff,
            "lastWin", match.getId() != null ? match.getId().toString() : "null"
        ));
        standingService.updateStanding(match.getEvent(), loser, 0, Map.of(
            "losses", 1, 
            "pointsDiff", -pointsDiff,
            "lastLoss", match.getId() != null ? match.getId().toString() : "null"
        ));
    }

    private int extractScore(Match match, UUID participantId) {
        if (match.getScore() != null && participantId != null) {
            Object s = match.getScore().get(participantId.toString());
            if (s instanceof Number n) return n.intValue();
            if (s instanceof String str) {
                try {
                    return Integer.parseInt(str);
                } catch (NumberFormatException ignored) {}
            }
        }
        return 0;
    }

    private void triggerAdvancementLogic(@NonNull Match match) {
        bracketService.advanceWinner(match);
    }

    @Transactional
    @NonNull
    public MatchResponse openDispute(@NonNull UUID matchId, @NonNull String reason) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found: " + matchId));

        match.setStatus(MatchStatus.DISPUTED);
        Map<String, Object> currentScore = match.getScore() != null ? new HashMap<>(match.getScore()) : new HashMap<>();
        currentScore.put("disputeReason", reason);
        match.setScore(currentScore);
        
        Match savedMatch = matchRepository.save(match);
        auditService.log("MATCH_DISPUTED", "MATCH", matchId, Map.of("reason", reason));
        
        return modelMapper.map(savedMatch, MatchResponse.class);
    }

    @Transactional
    @NonNull
    public MatchResponse resolveDispute(@NonNull UUID matchId, @NonNull MatchResultRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found: " + matchId));

        if (match.getStatus() != MatchStatus.DISPUTED) {
            throw new InvalidStateTransitionException("Only DISPUTED matches can be resolved.");
        }

        applyResultToMatch(match, request);
        
        if (match.getScore() != null) {
            Map<String, Object> scoreCopy = new HashMap<>(match.getScore());
            scoreCopy.put("disputeResolved", true);
            match.setScore(scoreCopy);
        }

        Match savedMatch = matchRepository.save(match);
        auditService.log("DISPUTE_RESOLVED", "MATCH", matchId, Map.of(
            "winner", request.getWinnerId() != null ? request.getWinnerId().toString() : "null"
        ));

        processStandingsUpdate(savedMatch);
        triggerAdvancementLogic(savedMatch);

        return modelMapper.map(savedMatch, MatchResponse.class);
    }
}

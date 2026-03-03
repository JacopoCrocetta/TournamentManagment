package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.dto.request.MatchResultRequest;
import com.tournamentmanagmentsystem.dto.response.MatchResponse;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import com.tournamentmanagmentsystem.service.bracket.SingleEliminationEngine;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Service for match result management and automation of tournament progress.
 * Updates scores, finishes matches, and triggers winner advancement in brackets.
 */
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final AuditService auditService;
    private final StandingService standingService;
    private final SingleEliminationEngine singleEliminationEngine;
    private final ModelMapper modelMapper;

    /**
     * Updates the result of a match, calculating standings and advancing winners.
     *
     * @param matchId match UUID
     * @param request result details (score, winner)
     * @return updated match data
     */
    @Transactional
    public MatchResponse updateResult(UUID matchId, MatchResultRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found: " + matchId));

        applyResultToMatch(match, request);
        Match savedMatch = matchRepository.save(match);

        logResultUpdate(savedMatch, request);
        processStandingsUpdate(savedMatch);
        triggerAdvancementLogic(savedMatch);

        return modelMapper.map(savedMatch, MatchResponse.class);
    }

    private void applyResultToMatch(Match match, MatchResultRequest request) {
        match.setScore(request.getScore());
        match.setWinnerId(request.getWinnerId());
        match.setStatus(MatchStatus.FINISHED);
    }

    private void logResultUpdate(Match match, MatchResultRequest request) {
        auditService.log("UPDATE_RESULT", "MATCH", match.getId(), Map.of(
                "winnerId", request.getWinnerId() != null ? request.getWinnerId().toString() : "null",
                "score", request.getScore() != null ? request.getScore() : Collections.emptyMap()));
    }

    private void processStandingsUpdate(Match match) {
        if (match.getWinnerId() == null) {
            return;
        }

        UUID winnerId = match.getWinnerId();

        Participant participantB = match.getParticipantB();

        if (participantA == null || participantB == null) {
            // Cannot update standings if one participant is missing (e.g. bye or TBD matches)
            return;
        }

        boolean isAWinner = Objects.equals(winnerId, participantA.getId());
        
        Participant winner = isAWinner ? participantA : participantB;
        Participant loser = isAWinner ? participantB : participantA;

        // 3 points for win, 0 for loss (League standard)
        standingService.updateStanding(match.getEvent(), winner, 3, Map.of("lastWin", match.getId()));
        standingService.updateStanding(match.getEvent(), loser, 0, Map.of("lastLoss", match.getId()));
    }

    private void triggerAdvancementLogic(Match match) {
        // Only applicable for Single Elimination brackets for now
        singleEliminationEngine.advanceWinner(match);
    }
}

package com.tournamentmanagmentsystem.service;

import org.springframework.lang.NonNull;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.dto.request.MatchResultRequest;
import com.tournamentmanagmentsystem.dto.response.MatchResponse;
import com.tournamentmanagmentsystem.exception.ResourceNotFoundException;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import com.tournamentmanagmentsystem.service.bracket.SingleEliminationEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
    private final SingleEliminationEngine singleEliminationEngine;
    private final ModelMapper modelMapper;

    @Transactional
    @NonNull
    public MatchResponse updateResult(@NonNull UUID matchId, @NonNull MatchResultRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));

        applyResultToMatch(Objects.requireNonNull(match), request);
        Match savedMatch = Objects.requireNonNull(matchRepository.save(match));

        logResultUpdate(savedMatch, request);
        processStandingsUpdate(savedMatch);
        triggerAdvancementLogic(savedMatch);

        log.info("Match result updated: ID={}, winnerID={}", savedMatch.getId(), request.getWinnerId());

        return Objects.requireNonNull(modelMapper.map(savedMatch, MatchResponse.class),
                "Mapped response must not be null");
    }

    private void applyResultToMatch(@NonNull Match match, @NonNull MatchResultRequest request) {
        match.setScore(request.getScore());
        match.setWinnerId(request.getWinnerId());
        match.setStatus(MatchStatus.FINISHED);
    }

    private void logResultUpdate(@NonNull Match match, @NonNull MatchResultRequest request) {
        if (match.getId() != null) {
            auditService.log("UPDATE_RESULT", "MATCH", Objects.requireNonNull(match.getId()),
                    Objects.requireNonNull(Map.of(
                            "winnerId", request.getWinnerId() != null ? request.getWinnerId().toString() : "null",
                            "score", request.getScore() != null ? request.getScore() : Collections.emptyMap())));
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

        standingService.updateStanding(Objects.requireNonNull(match.getEvent()), winner, 3,
                Map.of("lastWin", Objects.requireNonNull(match.getId())));
        standingService.updateStanding(Objects.requireNonNull(match.getEvent()), loser, 0,
                Map.of("lastLoss", Objects.requireNonNull(match.getId())));
    }

    private void triggerAdvancementLogic(@NonNull Match match) {
        singleEliminationEngine.advanceWinner(match);
    }
}

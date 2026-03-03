package com.tournamentmanagmentsystem.service.bracket;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.repository.ParticipantRepository;
import com.tournamentmanagmentsystem.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Orchestrator Service for bracket generation.
 * Coordinates between specific tournament engines and handles auditing of
 * bracket creation.
 */
@Service
@RequiredArgsConstructor
public class BracketService {

    private final SingleEliminationEngine singleEliminationEngine;
    private final RoundRobinEngine roundRobinEngine;
    private final ParticipantRepository participantRepository;
    private final AuditService auditService;

    /**
     * Generates a set of initial matches for an event based on the requested
     * format.
     *
     * @param eventId    the event UUID
     * @param formatType the tournament structure (Single Elimination, Round Robin,
     *                   etc.)
     * @return list of generated matches persisted in the database
     * @throws UnsupportedOperationException if the format is not yet implemented
     */
    @Transactional
    public List<Match> generateBracket(UUID eventId, FormatType formatType) {
        List<Participant> participants = participantRepository.findByTournamentId(eventId);

        List<Match> generatedMatches = dispatchToEngine(eventId, participants, formatType);

        logBracketGeneration(eventId, formatType, generatedMatches.size());

        return generatedMatches;
    }

    private List<Match> dispatchToEngine(UUID eventId, List<Participant> participants, FormatType formatType) {
        return switch (formatType) {
            case SINGLE_ELIMINATION -> singleEliminationEngine.generateInitialMatches(eventId, participants);
            case ROUND_ROBIN -> roundRobinEngine.generateInitialMatches(eventId, participants);
            default ->
                throw new UnsupportedOperationException("Generation logic not yet implemented for: " + formatType);
        };
    }

    private void logBracketGeneration(UUID eventId, FormatType formatType, int matchCount) {
        auditService.log("GENERATE_BRACKET", "EVENT", eventId, Map.of(
                "format", formatType,
                "matchCount", matchCount));
    }
}

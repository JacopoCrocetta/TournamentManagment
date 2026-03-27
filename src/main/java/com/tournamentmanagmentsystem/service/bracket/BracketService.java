package com.tournamentmanagmentsystem.service.bracket;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.exception.NotFoundException;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.ParticipantRepository;
import com.tournamentmanagmentsystem.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import com.tournamentmanagmentsystem.strategy.MatchmakingStrategy;
import com.tournamentmanagmentsystem.repository.MatchRepository;

/**
 * Orchestrator Service for bracket generation.
 * Coordinates between specific tournament engines and handles auditing of
 * bracket creation.
 */
@Service
@RequiredArgsConstructor
public class BracketService {

    private final Map<String, MatchmakingStrategy> matchmakingStrategies;
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final MatchRepository matchRepository;
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
        Event event = eventRepository.findById(Objects.requireNonNull(eventId))
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        
        UUID tournamentId = event.getTournament().getId();
        List<Participant> participants = participantRepository.findByTournamentId(Objects.requireNonNull(tournamentId)).stream()
                .filter(p -> Boolean.TRUE.equals(p.getCheckedIn()))
                .toList();

        List<Match> generatedMatches = dispatchToEngine(event, participants, formatType);

        logBracketGeneration(eventId, formatType, generatedMatches.size());

        return generatedMatches;
    }

    private List<Match> dispatchToEngine(Event event, List<Participant> participants, FormatType formatType) {
        MatchmakingStrategy strategy = matchmakingStrategies.get(formatType.name());
        if (strategy == null) {
            throw new UnsupportedOperationException("No strategy implemented for: " + formatType);
        }
        List<Match> matches = strategy.generateMatches(event, participants);
        return matchRepository.saveAll(matches);
    }

    // Removed advanceWinner: logic is now handled in MatchService for better encapsulation

    private void logBracketGeneration(UUID eventId, FormatType formatType, int matchCount) {
        auditService.log("GENERATE_BRACKET", "EVENT", Objects.requireNonNull(eventId), Objects.requireNonNull(Map.of(
                "format", formatType,
                "matchCount", matchCount)));
    }
}

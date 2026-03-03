package com.tournamentmanagmentsystem.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.entity.Standing;
import com.tournamentmanagmentsystem.repository.StandingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service for managing event leaderboards and participant rankings.
 * Handles point updates and tie-breaker statistics.
 */
@Service
@RequiredArgsConstructor
public class StandingService {

    private final StandingRepository standingRepository;

    /**
     * Updates or creates a standing for a participant in a specific event.
     *
     * @param event          the event parent
     * @param participant    the participant involved
     * @param pointsChange   the delta to apply to the score
     * @param tieBreakerInfo map of additional stats (e.g., number of wins, rounds)
     */
    @Transactional
    public void updateStanding(@NonNull Event event, @NonNull Participant participant, int pointsChange,
            @Nullable Map<String, Object> tieBreakerInfo) {
        Standing standing = standingRepository.findByEventIdAndParticipantId(event.getId(), participant.getId())
                .orElseGet(() -> createInitialStanding(event, participant));

        standing.setPoints(standing.getPoints() + pointsChange);

        if (tieBreakerInfo != null) {
            standing.getTieBreakerData().putAll(tieBreakerInfo);
        }

        standingRepository.save(standing);
    }

    /**
     * Retrieves the current standings for an event, ordered by points descending.
     *
     * @param eventId event UUID
     * @return ordered list of standings
     */
    @Transactional(readOnly = true)
    @NonNull
    public List<Standing> getStandings(@NonNull UUID eventId) {
        return standingRepository.findByEventIdOrderByPointsDesc(eventId);
    }

    private Standing createInitialStanding(@NonNull Event event, @NonNull Participant participant) {
        return Standing.builder()
                .event(event)
                .participant(participant)
                .points(0)
                .tieBreakerData(new HashMap<>())
                .build();
    }
}

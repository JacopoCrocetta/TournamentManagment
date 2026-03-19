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

        standing.setPoints((standing.getPoints() != null ? standing.getPoints() : 0) + pointsChange);

        if (tieBreakerInfo != null) {
            if (standing.getTieBreakerData() == null) {
                standing.setTieBreakerData(new HashMap<>());
            }
            final Map<String, Object> currentData = standing.getTieBreakerData();
            tieBreakerInfo.forEach((k, v) -> {
                if (v instanceof Number numUpdate) {
                    Object currentVal = currentData.get(k);
                    int currentInt = currentVal instanceof Number n ? n.intValue() : 0;
                    currentData.put(k, currentInt + numUpdate.intValue());
                } else {
                    currentData.put(k, v);
                }
            });
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
        List<Standing> standings = standingRepository.findByEventIdOrderByPointsDesc(eventId);
        standings.sort((s1, s2) -> {
            // 1. Points
            int p1 = s1.getPoints() != null ? s1.getPoints() : 0;
            int p2 = s2.getPoints() != null ? s2.getPoints() : 0;
            int pCmp = Integer.compare(p2, p1);
            if (pCmp != 0) return pCmp;
            
            // 2. Points Differential
            int diff1 = getTieBreakerInt(s1, "pointsDiff");
            int diff2 = getTieBreakerInt(s2, "pointsDiff");
            int diffCmp = Integer.compare(diff2, diff1);
            if (diffCmp != 0) return diffCmp;
            
            // 3. Wins
            int wins1 = getTieBreakerInt(s1, "wins");
            int wins2 = getTieBreakerInt(s2, "wins");
            return Integer.compare(wins2, wins1);
        });
        return standings;
    }

    private int getTieBreakerInt(Standing s, String key) {
        if (s.getTieBreakerData() == null) return 0;
        Object val = s.getTieBreakerData().get(key);
        if (val instanceof Number n) return n.intValue();
        return 0;
    }

    private Standing createInitialStanding(Event event, Participant participant) {
        return Standing.builder()
                .event(event)
                .participant(participant)
                .points(0)
                .tieBreakerData(new HashMap<>())
                .build();
    }
}

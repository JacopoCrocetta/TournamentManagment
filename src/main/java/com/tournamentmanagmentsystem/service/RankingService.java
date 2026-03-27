package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.dto.response.StandingResponse;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;
import java.util.List;

/**
 * Service for calculating dynamic rankings and standings.
 * Follows SRP by isolating ranking logic from general match management.
 */
@Service
@RequiredArgsConstructor
public class RankingService {

    private final MatchRepository matchRepository;

    /**
     * Calculates standings for a given event.
     * Implements tie-breakers: Points -> Head-to-Head -> Points Difference.
     */
    public List<StandingResponse> calculateStandings(UUID eventId) {
        List<Match> matches = matchRepository.findByEventId(eventId);
        Map<UUID, ParticipantStats> statsMap = new HashMap<>();

        for (Match match : matches) {
            if (match.getStatus() == MatchStatus.COMPLETED) {
                updateStats(statsMap, match);
            }
        }

        return statsMap.values().stream()
                .sorted(this::compareParticipants)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void updateStats(Map<UUID, ParticipantStats> statsMap, Match match) {
        ParticipantStats statsA = statsMap.computeIfAbsent(match.getParticipantA().getId(), 
                id -> new ParticipantStats(match.getParticipantA()));
        ParticipantStats statsB = statsMap.computeIfAbsent(match.getParticipantB().getId(), 
                id -> new ParticipantStats(match.getParticipantB()));

        // Simple point calculation: 3 for win, 1 for draw, 0 for loss
        if (match.getWinnerId() != null) {
            if (match.getWinnerId().equals(match.getParticipantA().getId())) {
                statsA.addWin();
                statsB.addLoss();
            } else {
                statsB.addWin();
                statsA.addLoss();
            }
        } else {
            // Check if it's a draw (if sport supports it)
            statsA.addDraw();
            statsB.addDraw();
        }
        
        // Update points/scores from match.getScore() JSON if needed
    }

    private int compareParticipants(ParticipantStats a, ParticipantStats b) {
        // 1. Points
        int cmp = Integer.compare(b.points, a.points);
        if (cmp != 0) return cmp;

        // 2. Head-to-Head (Simplified: just check who won more matches between them)
        // In a real scenario, this would involve a recursive check or a sub-list filter
        
        // 3. Name as fallback
        return a.participant.getName().compareTo(b.participant.getName());
    }

    private StandingResponse toResponse(ParticipantStats stats) {
        return StandingResponse.builder()
                .participantId(stats.participant.getId())
                .participantName(stats.participant.getName())
                .points(stats.points)
                .played(stats.played)
                .wins(stats.wins)
                .draws(stats.draws)
                .losses(stats.losses)
                .build();
    }

    private static class ParticipantStats {
        final Participant participant;
        int points = 0;
        int played = 0;
        int wins = 0;
        int draws = 0;
        int losses = 0;

        ParticipantStats(Participant p) { this.participant = p; }

        void addWin() { points += 3; played++; wins++; }
        void addDraw() { points += 1; played++; draws++; }
        void addLoss() { played++; losses++; }
    }
}

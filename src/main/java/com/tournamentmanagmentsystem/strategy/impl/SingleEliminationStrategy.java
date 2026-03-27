package com.tournamentmanagmentsystem.strategy.impl;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.strategy.MatchmakingStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Single Elimination matchmaking.
 * Handles BYEs for non-power-of-2 participants.
 */
@Component("SINGLE_ELIMINATION")
public class SingleEliminationStrategy implements MatchmakingStrategy {

    @Override
    public List<Match> generateMatches(Event event, List<Participant> participants) {
        if (participants == null || participants.isEmpty()) {
            return List.of();
        }

        int numParticipants = participants.size();
        int rounds = (int) Math.ceil(Math.log(numParticipants) / Math.log(2));

        List<Match> allMatches = new ArrayList<>();
        List<Match> currentRoundMatches = new ArrayList<>();
        List<Match> nextRoundMatches = new ArrayList<>();

        // 1. Create all matches for all rounds first to establish nextMatch links
        // We work backwards from the final to the first round
        Match finalMatch = createBaseMatch(event, rounds, "FINAL");
        allMatches.add(finalMatch);
        nextRoundMatches.add(finalMatch);

        for (int r = rounds - 1; r >= 1; r--) {
            currentRoundMatches.clear();
            int matchIndex = 0;
            for (Match nextMatch : nextRoundMatches) {
                // Each nextMatch has two source matches
                Match m1 = createBaseMatch(event, r, "R" + r + "_" + (matchIndex++));
                m1.setNextMatch(nextMatch);
                m1.setPositionInNextMatch(0);

                Match m2 = createBaseMatch(event, r, "R" + r + "_" + (matchIndex++));
                m2.setNextMatch(nextMatch);
                m2.setPositionInNextMatch(1);

                currentRoundMatches.add(m1);
                currentRoundMatches.add(m2);
                allMatches.add(m1);
                allMatches.add(m2);
            }
            nextRoundMatches = new ArrayList<>(currentRoundMatches);
        }

        // 2. Fill the first round with participants and handle BYEs
        // First round matches are the ones with r=1
        List<Match> firstRoundMatches = allMatches.stream()
                .filter(m -> m.getRoundNumber() == 1)
                .toList();

        // Seeding logic: Top seeds at extreme ends of bracket
        // For simplicity here, we just pair 1 vs N, 2 vs N-1 etc.
        for (int i = 0; i < firstRoundMatches.size(); i++) {
            Match match = firstRoundMatches.get(i);

            // Participant A
            if (i < participants.size()) {
                match.setParticipantA(participants.get(i));
            }

            // Participant B (paired from the end of the list to keep high seeds apart)
            int opponentIndex = participants.size() - 1 - i;
            if (opponentIndex > i && opponentIndex < participants.size()) {
                match.setParticipantB(participants.get(opponentIndex));
            } else {
                // BYE logic: If no opponent, participant A wins automatically
                handleBye(match);
            }
        }

        return allMatches;
    }

    private Match createBaseMatch(Event event, int round, String stage) {
        return Match.builder()
                .event(event)
                .roundNumber(round)
                .stage(stage)
                .status(MatchStatus.SCHEDULED)
                .build();
    }

    private void handleBye(Match match) {
        if (match.getParticipantA() != null) {
            match.setStatus(MatchStatus.COMPLETED);
            match.setWinnerId(match.getParticipantA().getId());
            // Advancement logic will be handled by MatchService when result is "updated"
        }
    }
}

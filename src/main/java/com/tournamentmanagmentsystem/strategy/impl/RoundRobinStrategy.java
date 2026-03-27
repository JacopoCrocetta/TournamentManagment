package com.tournamentmanagmentsystem.strategy.impl;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.strategy.MatchmakingStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Round Robin matchmaking using Berger Algorithm.
 */
@Component("ROUND_ROBIN")
public class RoundRobinStrategy implements MatchmakingStrategy {

    @Override
    public List<Match> generateMatches(Event event, List<Participant> participants) {
        if (participants == null || participants.size() < 2) {
            return List.of();
        }

        List<Participant> pool = new ArrayList<>(participants);
        if (pool.size() % 2 != 0) {
            pool.add(null); // Ghost participant for BYE
        }

        int numParticipants = pool.size();
        int numRounds = numParticipants - 1;
        int matchesPerRound = numParticipants / 2;
        
        List<Match> allMatches = new ArrayList<>();

        for (int round = 0; round < numRounds; round++) {
            for (int j = 0; j < matchesPerRound; j++) {
                Participant p1 = pool.get(j);
                Participant p2 = pool.get(numParticipants - 1 - j);

                if (p1 != null && p2 != null) {
                    allMatches.add(Match.builder()
                            .event(event)
                            .roundNumber(round + 1)
                            .stage("ROUND_ROBIN")
                            .participantA(p1)
                            .participantB(p2)
                            .status(MatchStatus.SCHEDULED)
                            .build());
                }
            }
            // Rotate the pool (keeping the first participant fixed)
            Participant last = pool.remove(numParticipants - 1);
            pool.add(1, last);
        }

        return allMatches;
    }
}

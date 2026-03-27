package com.tournamentmanagmentsystem.strategy.impl;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SingleEliminationStrategyTest {

    private SingleEliminationStrategy strategy;
    private Event event;

    @BeforeEach
    void setUp() {
        strategy = new SingleEliminationStrategy();
        event = Event.builder().id(UUID.randomUUID()).build();
    }

    @Test
    void generateMatches_PowerOfTwo_Success() {
        List<Participant> participants = createParticipants(4);
        
        List<Match> matches = strategy.generateMatches(event, participants);
        
        // 4 participants -> 3 matches total
        assertEquals(3, matches.size());
        
        // Check final match
        Match finalMatch = matches.stream().filter(m -> "FINAL".equals(m.getStage())).findFirst().orElseThrow();
        assertNull(finalMatch.getNextMatch());
        
        // Check round 1 matches
        List<Match> round1 = matches.stream().filter(m -> m.getRoundNumber() == 1).toList();
        assertEquals(2, round1.size());
        for (Match m : round1) {
            assertEquals(finalMatch, m.getNextMatch());
            assertNotNull(m.getParticipantA());
            assertNotNull(m.getParticipantB());
        }
    }

    @Test
    void generateMatches_NonPowerOfTwo_HandlesByes() {
        List<Participant> participants = createParticipants(3);
        
        List<Match> matches = strategy.generateMatches(event, participants);
        
        // 3 participants -> 4 slots needed in first round -> 3 matches total (2 rounds)
        assertEquals(3, matches.size());
        
        List<Match> round1 = matches.stream().filter(m -> m.getRoundNumber() == 1).toList();
        
        // One match should be a BYE (completed status)
        long byes = round1.stream().filter(m -> m.getStatus() == MatchStatus.COMPLETED).count();
        assertEquals(1, byes);
        
        Match byeMatch = round1.stream().filter(m -> m.getStatus() == MatchStatus.COMPLETED).findFirst().orElseThrow();
        assertNotNull(byeMatch.getWinnerId());
        assertEquals(byeMatch.getParticipantA().getId(), byeMatch.getWinnerId());
    }

    private List<Participant> createParticipants(int count) {
        List<Participant> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(Participant.builder()
                    .id(UUID.randomUUID())
                    .name("Player " + i)
                    .build());
        }
        return list;
    }
}

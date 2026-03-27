package com.tournamentmanagmentsystem.strategy.impl;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoundRobinStrategyTest {

    private RoundRobinStrategy strategy;
    private Event event;

    @BeforeEach
    void setUp() {
        strategy = new RoundRobinStrategy();
        event = Event.builder().id(UUID.randomUUID()).build();
    }

    @Test
    void generateMatches_EvenParticipants_Success() {
        List<Participant> participants = createParticipants(4);
        
        List<Match> matches = strategy.generateMatches(event, participants);
        
        // 4 participants -> 3 rounds, 2 matches per round -> 6 matches total
        assertEquals(6, matches.size());
        
        long rounds = matches.stream().map(Match::getRoundNumber).distinct().count();
        assertEquals(3, rounds);
    }

    @Test
    void generateMatches_OddParticipants_HandlesGhost() {
        List<Participant> participants = createParticipants(3);
        
        List<Match> matches = strategy.generateMatches(event, participants);
        
        // 3 participants (+1 ghost) -> 3 rounds, 1 real match per round -> 3 matches total
        assertEquals(3, matches.size());
        
        long rounds = matches.stream().map(Match::getRoundNumber).distinct().count();
        assertEquals(3, rounds);
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

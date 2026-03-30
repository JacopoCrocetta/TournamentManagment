package com.tournamentmanagmentsystem.service.bracket;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoundRobinEngineTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private RoundRobinEngine engine;

    private UUID eventId;
    private Event event;
    private List<Participant> participants;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        event = new Event();
        event.setId(eventId);

        participants = List.of(
                Participant.builder().id(UUID.randomUUID()).name("P1").build(),
                Participant.builder().id(UUID.randomUUID()).name("P2").build(),
                Participant.builder().id(UUID.randomUUID()).name("P3").build());
    }

    @Test
    void generateInitialMatches_Success() {
        when(eventRepository.findById(java.util.Objects.requireNonNull(eventId))).thenReturn(Optional.of(event));
        when(matchRepository.saveAll(java.util.Objects.requireNonNull(anyList())))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<Match> results = engine.generateInitialMatches(java.util.Objects.requireNonNull(eventId),
                java.util.Objects.requireNonNull(participants));

        assertNotNull(results);
        // For 3 participants, we expect n*(n-1)/2 matches = 3*(2)/2 = 3 matches
        // P1 vs P2, P1 vs P3, P2 vs P3
        assertEquals(3, results.size());
        verify(matchRepository).saveAll(java.util.Objects.requireNonNull(anyList()));
    }

    @Test
    void advanceWinner_ReturnsNull() {
        Match result = engine.advanceWinner(new Match());
        assertNull(result);
    }
}

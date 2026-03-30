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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SingleEliminationEngineTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private SingleEliminationEngine engine;

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
                Participant.builder().id(UUID.randomUUID()).name("P3").build(),
                Participant.builder().id(UUID.randomUUID()).name("P4").build());
    }

    @Test
    void generateInitialMatches_Success() {
        when(eventRepository.findById(Objects.requireNonNull(eventId))).thenReturn(Optional.of(event));
        when(matchRepository.saveAll(java.util.Objects.requireNonNull(anyList()))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Match> results = engine.generateInitialMatches(Objects.requireNonNull(eventId),
                Objects.requireNonNull(participants));

        assertNotNull(results);
        assertEquals(2, results.size()); // 4 participants / 2 = 2 matches
        verify(matchRepository, times(1)).saveAll(java.util.Objects.requireNonNull(anyList()));
    }

    @Test
    void generateInitialMatches_OddNumber_WithBye() {
        List<Participant> oddParticipants = participants.subList(0, 3); // 3 participants
        when(eventRepository.findById(Objects.requireNonNull(eventId))).thenReturn(Optional.of(event));
        when(matchRepository.saveAll(java.util.Objects.requireNonNull(anyList()))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Match> results = engine.generateInitialMatches(Objects.requireNonNull(eventId),
                Objects.requireNonNull(oddParticipants));

        assertEquals(2, results.size()); // 1 match + 1 bye match
        long byes = results.stream().filter(m -> m.getParticipantB() == null).count();
        assertEquals(1, byes);
    }

    @Test
    void advanceWinner_NotImplementedYet() {
        Match match = new Match();
        Match result = engine.advanceWinner(match);
        assertNull(result);
    }
}

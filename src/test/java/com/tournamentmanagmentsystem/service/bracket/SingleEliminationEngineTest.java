package com.tournamentmanagmentsystem.service.bracket;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
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
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(matchRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Match> results = engine.generateInitialMatches(eventId, participants);

        assertNotNull(results);
        assertEquals(3, results.size()); // 4 participants -> 3 matches
        verify(matchRepository, times(1)).saveAll(anyList());
    }

    @Test
    void generateInitialMatches_OddNumber_WithBye() {
        List<Participant> oddParticipants = participants.subList(0, 3); // 3 participants
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(matchRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Match> results = engine.generateInitialMatches(eventId, oddParticipants);

        assertEquals(3, results.size()); // 4 placeholder for 3 participants = 3 matches
        long byes = results.stream().filter(m -> (m.getParticipantA() == null || m.getParticipantB() == null) && m.getRoundNumber() == 1).count();
        assertEquals(1, byes);
    }

    @Test
    void advanceWinner_Success() {
        Match m1 = Match.builder()
                .event(event)
                .stage("1_0")
                .winnerId(participants.get(0).getId())
                .participantA(participants.get(0))
                .build();
                
        Match nextMatch = Match.builder()
                .event(event)
                .stage("2_0")
                .build();
                
        when(matchRepository.findByEventIdAndStage(eventId, "2_0")).thenReturn(Optional.of(nextMatch));
        when(matchRepository.save(any(Match.class))).thenAnswer(i -> i.getArgument(0));

        Match result = engine.advanceWinner(m1);

        assertNotNull(result);
        assertEquals(participants.get(0), result.getParticipantA());
        verify(matchRepository).save(result);
    }
}

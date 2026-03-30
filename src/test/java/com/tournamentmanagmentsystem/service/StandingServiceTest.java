package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.entity.Standing;
import com.tournamentmanagmentsystem.repository.StandingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class StandingServiceTest {

    @Mock
    private StandingRepository standingRepository;

    @InjectMocks
    private StandingService standingService;

    private Event event;
    private Participant participant;
    private UUID eventId;
    private UUID participantId;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        participantId = UUID.randomUUID();
        event = new Event();
        event.setId(eventId);
        participant = Participant.builder().id(participantId).build();
    }

    @Test
    void updateStanding_NewStanding_Success() {
        when(standingRepository.findByEventIdAndParticipantId(eventId, participantId))
                .thenReturn(Optional.empty());
        when(standingRepository.save(any(Standing.class))).thenAnswer(i -> i.getArguments()[0]);

        standingService.updateStanding(event, participant, 3, Map.of("won", 1));

        verify(standingRepository).save(any(Standing.class));
    }

    @Test
    void updateStanding_ExistingStanding_Success() {
        Standing existing = Standing.builder()
                .event(event)
                .participant(participant)
                .points(5)
                .tieBreakerData(new HashMap<>(Map.of("won", 1)))
                .build();

        when(standingRepository.findByEventIdAndParticipantId(eventId, participantId))
                .thenReturn(Optional.of(existing));

        standingService.updateStanding(event, participant, 3, Map.of("won", 2));

        assertEquals(8, existing.getPoints());
        assertEquals(2, existing.getTieBreakerData().get("won"));
        verify(standingRepository).save(existing);
    }
}

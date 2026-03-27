package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.entity.Standing;
import com.tournamentmanagmentsystem.dto.response.StandingResponse;
import com.tournamentmanagmentsystem.mapper.StandingMapper;
import com.tournamentmanagmentsystem.repository.StandingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandingServiceTest {

    @Mock
    private StandingRepository standingRepository;
    @Mock
    private StandingMapper standingMapper;

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
        when(standingRepository.findByEventIdAndParticipantId(Objects.requireNonNull(eventId), Objects.requireNonNull(participantId)))
                .thenReturn(Optional.empty());
        when(standingRepository.save(any(Standing.class))).thenAnswer(i -> Objects.requireNonNull(i.getArguments()[0]));

        standingService.updateStanding(Objects.requireNonNull(event), Objects.requireNonNull(participant), 3, Objects.requireNonNull(Map.of("won", 1)));

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

        when(standingRepository.findByEventIdAndParticipantId(Objects.requireNonNull(eventId), Objects.requireNonNull(participantId)))
                .thenReturn(Optional.of(existing));

        standingService.updateStanding(Objects.requireNonNull(event), Objects.requireNonNull(participant), 3, Objects.requireNonNull(Map.of("won", 2)));

        verify(standingRepository).save(Objects.requireNonNull(existing));
    }

    @Test
    void getStandings_Success() {
        UUID eventId = UUID.randomUUID();
        Standing standing = Standing.builder().points(10).build();
        when(standingRepository.findByEventIdOrderByPointsDesc(eventId)).thenReturn(new java.util.ArrayList<>(java.util.List.of(standing)));
        when(standingMapper.toResponse(any())).thenReturn(new StandingResponse());

        List<StandingResponse> results = standingService.getStandings(eventId);

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(standingMapper).toResponse(any());
    }
}

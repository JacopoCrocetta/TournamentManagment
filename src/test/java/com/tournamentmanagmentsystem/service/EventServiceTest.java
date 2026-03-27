package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.dto.request.EventRequest;
import com.tournamentmanagmentsystem.dto.response.EventResponse;
import com.tournamentmanagmentsystem.domain.enums.EventStatus;
import com.tournamentmanagmentsystem.exception.NotFoundException;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EventService eventService;

    private UUID tournamentId;
    private Tournament tournament;
    private EventRequest request;
    private Event event;

    @BeforeEach
    void setUp() {
        tournamentId = UUID.randomUUID();
        tournament = new Tournament();
        tournament.setId(tournamentId);

        request = new EventRequest();
        request.setTournamentId(tournamentId);
        request.setName("Event 1");

        event = new Event();
        event.setId(UUID.randomUUID());
        event.setName("Event 1");
    }

    @Test
    void createEvent_Success() {
        when(tournamentRepository.findById(Objects.requireNonNull(tournamentId))).thenReturn(Optional.of(Objects.requireNonNull(tournament)));
        // ModelMapper stub for Event.class is no longer needed as we map manually
        when(eventRepository.save(any(Event.class))).thenReturn(Objects.requireNonNull(event));
        when(modelMapper.map(any(), eq(EventResponse.class))).thenReturn(Objects.requireNonNull(new EventResponse()));

        EventResponse response = eventService.createEvent(request);

        assertNotNull(response);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void createEvent_TournamentNotFound_ThrowsException() {
        when(tournamentRepository.findById(Objects.requireNonNull(tournamentId))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.createEvent(request));
        verify(eventRepository, never()).save(any());
    }
}

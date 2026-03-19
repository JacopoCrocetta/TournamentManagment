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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(modelMapper.map(request, Event.class)).thenReturn(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(modelMapper.map(event, EventResponse.class)).thenReturn(new EventResponse());

        EventResponse response = eventService.createEvent(request);

        assertNotNull(response);
        verify(eventRepository).save(event);
        assertEquals(tournament, event.getTournament());
        assertEquals(EventStatus.DRAFT, event.getStatus());
    }

    @Test
    void createEvent_TournamentNotFound_ThrowsException() {
        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.createEvent(request));
        verify(eventRepository, never()).save(any());
    }
}

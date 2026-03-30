package com.tournamentmanagmentsystem.service;

import org.springframework.lang.NonNull;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.dto.request.EventRequest;
import com.tournamentmanagmentsystem.dto.response.EventResponse;
import com.tournamentmanagmentsystem.exception.BusinessException;
import com.tournamentmanagmentsystem.exception.ResourceNotFoundException;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing tournament events (sub-competitions).
 * Handles the creation and retrieval of event configurations.
 */
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final TournamentRepository tournamentRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new event linked to an existing tournament.
     *
     * @param request event configuration (name, format, etc.)
     * @return the created event details
     * @throws RuntimeException if tournament is not found
     */
    @Transactional
    @NonNull
    public EventResponse createEvent(@NonNull EventRequest request) {
        Tournament tournament = tournamentRepository.findById(Objects.requireNonNull(request.getTournamentId()))
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found: " + request.getTournamentId()));

        Event event = modelMapper.map(request, Event.class);
        if (event == null) {
            throw new BusinessException("Mapping error during event creation");
        }

        event.setTournament(tournament);
        event.setStatus("DRAFT"); // Standard start status

        Event savedEvent = eventRepository.save(event);
        return Objects.requireNonNull(modelMapper.map(savedEvent, EventResponse.class));
    }

    /**
     * Retrieves all events associated with a specific tournament.
     *
     * @param tournamentId the parent tournament UUID
     * @return list of event details
     */
    @Transactional(readOnly = true)
    @NonNull
    public List<EventResponse> getEventsByTournament(@NonNull UUID tournamentId) {
        return Objects.requireNonNull(eventRepository.findByTournamentId(tournamentId).stream()
                .map(event -> modelMapper.map(event, EventResponse.class))
                .collect(Collectors.toList()));
    }

    /**
     * Retrieves detail for a specific event.
     *
     * @param id the event UUID
     * @return event details
     * @throws RuntimeException if event not found
     */
    @Transactional(readOnly = true)
    @NonNull
    public EventResponse getEvent(@NonNull UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
        return Objects.requireNonNull(modelMapper.map(event, EventResponse.class));
    }
}

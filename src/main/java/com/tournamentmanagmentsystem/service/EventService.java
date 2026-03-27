package com.tournamentmanagmentsystem.service;

import org.springframework.lang.NonNull;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.enums.EventStatus;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.dto.request.EventRequest;
import com.tournamentmanagmentsystem.dto.response.EventResponse;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import com.tournamentmanagmentsystem.mapper.EventMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.tournamentmanagmentsystem.exception.NotFoundException;

/**
 * Service for managing tournament events (sub-competitions).
 * Handles the creation and retrieval of event configurations.
 */
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final TournamentRepository tournamentRepository;
    private final EventMapper eventMapper;

    /**
     * Creates a new event linked to an existing tournament.
     *
     * @param request event configuration (name, format, etc.)
     * @return the created event details
     * @throws NotFoundException if tournament is not found
     */
    @Transactional
    @NonNull
    public EventResponse createEvent(@NonNull EventRequest request) {
        Tournament tournament = tournamentRepository.findById(Objects.requireNonNull(request.getTournamentId()))
                .orElseThrow(() -> new NotFoundException("Tournament not found: " + request.getTournamentId()));

        Event event = Event.builder()
                .name(request.getName())
                .tournament(tournament)
                .formatType(request.getFormatType())
                .seedingPolicy(request.getSeedingPolicy())
                .status(EventStatus.DRAFT)
                .build();

        Event savedEvent = eventRepository.save(event);
        return eventMapper.toResponse(savedEvent);
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
        return eventRepository.findByTournamentId(Objects.requireNonNull(tournamentId)).stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves detail for a specific event.
     *
     * @param id the event UUID
     * @return event details
     * @throws NotFoundException if event not found
     */
    @Transactional(readOnly = true)
    @NonNull
    public EventResponse getEvent(@NonNull UUID id) {
        Event event = eventRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NotFoundException("Event not found: " + id));
        return eventMapper.toResponse(event);
    }
}

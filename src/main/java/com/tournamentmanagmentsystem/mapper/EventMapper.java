package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.dto.response.EventResponse;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public EventResponse toResponse(Event event) {
        if (event == null) return null;
        return EventResponse.builder()
                .id(event.getId())
                .tournamentId(event.getTournament() != null ? event.getTournament().getId() : null)
                .name(event.getName())
                .formatType(event.getFormatType())
                .seedingPolicy(event.getSeedingPolicy())
                .maxParticipants(event.getMaxParticipants())
                .status(event.getStatus())
                .config(event.getConfig())
                .build();
    }
}

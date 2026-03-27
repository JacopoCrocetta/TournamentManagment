package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.dto.response.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "tournamentId", source = "tournament.id")
    EventResponse toResponse(Event event);
}

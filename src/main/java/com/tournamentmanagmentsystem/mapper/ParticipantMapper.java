package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.dto.response.ParticipantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(target = "tournamentId", source = "tournament.id")
    @Mapping(target = "eventId", source = "event.id")
    ParticipantResponse toResponse(Participant participant);
}

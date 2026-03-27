package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.dto.response.TournamentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentMapper {
    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "currentParticipants", ignore = true)
    TournamentResponse toResponse(Tournament tournament);
}

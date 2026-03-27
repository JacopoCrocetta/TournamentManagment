package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.dto.response.MatchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "participantAName", source = "participantA.name")
    @Mapping(target = "participantBName", source = "participantB.name")
    MatchResponse toResponse(Match match);
}

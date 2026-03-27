package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.dto.response.TournamentResponse;
import org.springframework.stereotype.Component;

@Component
public class TournamentMapper {
    public TournamentResponse toResponse(Tournament tournament) {
        if (tournament == null) return null;
        return TournamentResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .description(tournament.getDescription())
                .organizationId(tournament.getOrganization() != null ? tournament.getOrganization().getId() : null)
                .sportType(tournament.getSportType())
                .status(tournament.getStatus())
                .format(tournament.getFormat())
                .visibility(tournament.getVisibility())
                .seedingPolicy(tournament.getSeedingPolicy())
                .maxParticipants(tournament.getMaxParticipants())
                // In a real app, currentParticipants might be calculated via a ParticipantRepository.countByTournamentId
                .currentParticipants(0) 
                .startDate(tournament.getStartDate())
                .endDate(tournament.getEndDate())
                .createdAt(tournament.getCreatedAt())
                .build();
    }
}

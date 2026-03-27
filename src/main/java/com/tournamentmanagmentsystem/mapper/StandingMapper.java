package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Standing;
import com.tournamentmanagmentsystem.dto.response.StandingResponse;
import org.springframework.stereotype.Component;

@Component
public class StandingMapper {
    public StandingResponse toResponse(Standing standing) {
        if (standing == null) return null;
        return StandingResponse.builder()
                .id(standing.getId())
                .eventId(standing.getEvent() != null ? standing.getEvent().getId() : null)
                .participantId(standing.getParticipant() != null ? standing.getParticipant().getId() : null)
                .participantName(standing.getParticipant() != null ? standing.getParticipant().getName() : null)
                .points(standing.getPoints())
                .tieBreakerData(standing.getTieBreakerData())
                .build();
    }
}

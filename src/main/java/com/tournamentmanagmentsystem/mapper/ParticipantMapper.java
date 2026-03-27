package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.dto.response.ParticipantResponse;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper {
    public ParticipantResponse toResponse(Participant participant) {
        if (participant == null) return null;
        return ParticipantResponse.builder()
                .id(participant.getId())
                .tournamentId(participant.getTournament() != null ? participant.getTournament().getId() : null)
                .eventId(participant.getEvent() != null ? participant.getEvent().getId() : null)
                .name(participant.getName())
                .type(participant.getType())
                .seed(participant.getSeed())
                .rating(participant.getRating())
                .build();
    }
}

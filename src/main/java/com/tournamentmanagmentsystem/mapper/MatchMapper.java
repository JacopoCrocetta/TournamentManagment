package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.dto.response.MatchResponse;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {
    public MatchResponse toResponse(Match match) {
        if (match == null) return null;
        return MatchResponse.builder()
                .id(match.getId())
                .eventId(match.getEvent() != null ? match.getEvent().getId() : null)
                .stage(match.getStage())
                .roundNumber(match.getRoundNumber())
                .participantAName(match.getParticipantA() != null ? match.getParticipantA().getName() : null)
                .participantBName(match.getParticipantB() != null ? match.getParticipantB().getName() : null)
                .status(match.getStatus())
                .score(match.getScore())
                .winnerId(match.getWinnerId())
                .scheduledStart(match.getScheduledStart())
                .build();
    }
}

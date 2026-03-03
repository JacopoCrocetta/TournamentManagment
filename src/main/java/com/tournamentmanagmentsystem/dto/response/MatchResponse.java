package com.tournamentmanagmentsystem.dto.response;

import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponse {
    private UUID id;
    private UUID eventId;
    private String stage;
    private Integer roundNumber;
    private String participantAName;
    private String participantBName;
    private MatchStatus status;
    private Map<String, Object> score;
    private UUID winnerId;
    private LocalDateTime scheduledStart;
}

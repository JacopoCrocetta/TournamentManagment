package com.tournamentmanagmentsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandingResponse {
    private UUID id;
    private UUID eventId;
    private UUID participantId;
    private String participantName;
    private Integer points;
    private Integer played;
    private Integer wins;
    private Integer draws;
    private Integer losses;
    private Map<String, Object> tieBreakerData;
}

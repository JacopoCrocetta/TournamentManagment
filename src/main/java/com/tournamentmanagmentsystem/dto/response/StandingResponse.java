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
    private Map<String, Object> tieBreakerData;
}

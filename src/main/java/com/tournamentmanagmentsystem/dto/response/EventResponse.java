package com.tournamentmanagmentsystem.dto.response;

import com.tournamentmanagmentsystem.domain.enums.EventStatus;
import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.domain.enums.SeedingPolicy;
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
public class EventResponse {
    private UUID id;
    private UUID tournamentId;
    private String name;
    private FormatType formatType;
    private SeedingPolicy seedingPolicy;
    private Integer maxParticipants;
    private EventStatus status;
    private Map<String, Object> config;
}

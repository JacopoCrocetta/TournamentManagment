package com.tournamentmanagmentsystem.dto.response;

import com.tournamentmanagmentsystem.domain.enums.ParticipantStatus;
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
public class ParticipantResponse {
    private UUID id;
    private UUID tournamentId;
    private String name;
    private ParticipantStatus status;
    private Integer seed;
    private Boolean checkedIn;
    private Map<String, Object> metadata;
}

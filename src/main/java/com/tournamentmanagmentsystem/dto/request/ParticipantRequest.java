package com.tournamentmanagmentsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ParticipantRequest {
    @NotNull
    private UUID tournamentId;

    @NotBlank
    private String name;

    // Optional user link
    private UUID userId;

    private UUID teamId;

    // Dynamic metadata (JSONB)
    private Map<String, Object> metadata;
}

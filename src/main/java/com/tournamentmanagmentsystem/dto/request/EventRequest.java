package com.tournamentmanagmentsystem.dto.request;

import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.domain.enums.SeedingPolicy;
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
public class EventRequest {
    @NotNull
    private UUID tournamentId;

    @NotBlank
    private String name;

    @NotNull
    private FormatType formatType;

    @NotNull
    private SeedingPolicy seedingPolicy;

    private Integer maxParticipants;

    private Map<String, Object> config;
}

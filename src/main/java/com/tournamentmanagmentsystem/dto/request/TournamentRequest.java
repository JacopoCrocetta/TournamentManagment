package com.tournamentmanagmentsystem.dto.request;

import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.domain.enums.SeedingPolicy;
import com.tournamentmanagmentsystem.domain.enums.TournamentVisibility;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private UUID organizationId;

    @NotNull
    private FormatType format;

    @NotNull
    private TournamentVisibility visibility;

    @NotNull
    private SeedingPolicy seedingPolicy;

    @Min(2)
    private int maxParticipants;

    @NotBlank
    private String sportType;

    private String location;
    private String timezone;

    @FutureOrPresent
    private LocalDateTime startDate;

    private LocalDateTime endDate;
}

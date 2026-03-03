package com.tournamentmanagmentsystem.dto.response;

import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.domain.enums.SeedingPolicy;
import com.tournamentmanagmentsystem.domain.enums.TournamentStatus;
import com.tournamentmanagmentsystem.domain.enums.TournamentVisibility;
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
public class TournamentResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID organizationId;
    private TournamentStatus status;
    private FormatType format;
    private TournamentVisibility visibility;
    private SeedingPolicy seedingPolicy;
    private int maxParticipants;
    private int currentParticipants;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
}

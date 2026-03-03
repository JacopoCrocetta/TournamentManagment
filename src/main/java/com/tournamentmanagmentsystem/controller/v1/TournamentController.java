package com.tournamentmanagmentsystem.controller.v1;

import com.tournamentmanagmentsystem.domain.enums.TournamentStatus;
import com.tournamentmanagmentsystem.dto.request.TournamentRequest;
import com.tournamentmanagmentsystem.dto.response.TournamentResponse;
import com.tournamentmanagmentsystem.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing Tournaments.
 * Provides endpoints for tournament creation, details retrieval, and status
 * management.
 */
@RestController
@RequestMapping("/api/v1/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournaments", description = "Operations related to tournament lifecycles within Organizations")
@SecurityRequirement(name = "bearerAuth")
public class TournamentController {

    private final TournamentService tournamentService;

    /**
     * Creates a new tournament in an organization.
     * Restricted to ORG_ADMIN of the parent organization.
     *
     * @param request tournament creation details
     * @return the created tournament details
     */
    @PostMapping
    @Operation(summary = "Create a new tournament", description = "Initializes a new tournament in DRAFT status. Requires ORG_ADMIN role in the specified organization.", responses = {
            @ApiResponse(responseCode = "200", description = "Tournament successfully created"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ORG_ADMIN role", content = @Content)
    })
    @PreAuthorize("@securityService.hasRoleInOrganization(#request.organizationId, 'ORG_ADMIN')")
    @NonNull
    public ResponseEntity<TournamentResponse> create(@Valid @RequestBody @NonNull TournamentRequest request) {
        return ResponseEntity.ok(tournamentService.createTournament(request));
    }

    /**
     * Retrieves the details of a specific tournament.
     *
     * @param id tournament UUID
     * @return tournament details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get tournament details", description = "Returns the configuration and status of a specific tournament.")
    @NonNull
    public ResponseEntity<TournamentResponse> getById(@PathVariable @NonNull UUID id) {
        return ResponseEntity.ok(tournamentService.getTournament(id));
    }

    /**
     * Lists all tournaments belonging to a specific organization.
     *
     * @param orgId organization UUID
     * @return list of tournaments
     */
    @GetMapping("/organization/{orgId}")
    @Operation(summary = "List tournaments by organization", description = "Fetches all tournaments hosted by the specified organization. Requires VIEWER role in the organization.")
    @PreAuthorize("@securityService.hasRoleInOrganization(#orgId, 'VIEWER')")
    @NonNull
    public ResponseEntity<List<TournamentResponse>> getByOrg(@PathVariable @NonNull UUID orgId) {
        return ResponseEntity.ok(tournamentService.getTournamentsByOrganization(orgId));
    }

    /**
     * Updates the status of a tournament (e.g., from DRAFT to REGISTRATION_OPEN).
     * Restricted to TOURNAMENT_ADMIN.
     *
     * @param id     tournament UUID
     * @param status the new target status
     * @return updated tournament details
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Change tournament status", description = "Transitions the tournament to a new state (e.g., opening registration or starting the event). Restricted to TOURNAMENT_ADMIN.", responses = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires TOURNAMENT_ADMIN role", content = @Content)
    })
    @PreAuthorize("@securityService.hasRoleInTournament(#id, 'TOURNAMENT_ADMIN')")
    @NonNull
    public ResponseEntity<TournamentResponse> updateStatus(
            @PathVariable @NonNull UUID id,
            @Parameter(description = "The new status to apply") @RequestParam @NonNull TournamentStatus status) {
        return ResponseEntity.ok(tournamentService.updateStatus(id, status));
    }
}

package com.tournamentmanagmentsystem.controller.v1;

import com.tournamentmanagmentsystem.dto.request.EventRequest;
import com.tournamentmanagmentsystem.dto.response.EventResponse;
import com.tournamentmanagmentsystem.service.EventService;
import com.tournamentmanagmentsystem.service.StandingService;
import com.tournamentmanagmentsystem.domain.entity.Standing;
import io.swagger.v3.oas.annotations.Operation;
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
 * REST controller for tournament Events and Leaderboards.
 * Manages the lifecycle of sub-competitions and exposes live results/standings.
 */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Events & Standings", description = "Operations for event management and leaderboard retrieval")
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    private final EventService eventService;
    private final StandingService standingService;

    /**
     * Creates a new event for a specified tournament.
     * Requires TOURNAMENT_ADMIN permission.
     *
     * @param request event configuration details
     * @return the created event details
     */
    @PostMapping
    @Operation(summary = "Create an event", description = "Adds a new sub-competition (event) to a tournament. Requires TOURNAMENT_ADMIN role.", responses = {
            @ApiResponse(responseCode = "200", description = "Event created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required", content = @Content)
    })
    @PreAuthorize("@securityService.hasRoleInTournament(#request.tournamentId, 'TOURNAMENT_ADMIN')")
    public ResponseEntity<EventResponse> create(@Valid @RequestBody @NonNull EventRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    /**
     * Lists all events hosted by a specific tournament.
     *
     * @param tournamentId parent tournament UUID
     * @return list of events
     */
    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "List events by tournament", description = "Fetches all events configured for the given tournament.")
    public ResponseEntity<List<EventResponse>> getByTournament(@PathVariable @NonNull UUID tournamentId) {
        return ResponseEntity.ok(eventService.getEventsByTournament(tournamentId));
    }

    /**
     * Retrieves details for a specific event.
     *
     * @param id event UUID
     * @return event details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID", description = "Returns full details including format and status of a specific event.")
    public ResponseEntity<EventResponse> getById(@PathVariable @NonNull UUID id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    /**
     * Retrieves the current standings/leaderboard for an event.
     * Generally public or VIEWER access.
     *
     * @param id event UUID
     * @return ordered list of participant standings
     */
    @GetMapping("/{id}/standings")
    @Operation(summary = "Get event standings", description = "Returns the live leaderboard for an event, ordered by points. Each entry includes participant info and tie-breaker statistics.")
    public ResponseEntity<List<Standing>> getStandings(@PathVariable @NonNull UUID id) {
        return ResponseEntity.ok(standingService.getStandings(id));
    }
}

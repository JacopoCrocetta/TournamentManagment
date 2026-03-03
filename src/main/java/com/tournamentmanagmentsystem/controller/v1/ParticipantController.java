package com.tournamentmanagmentsystem.controller.v1;

import com.tournamentmanagmentsystem.dto.request.ParticipantRequest;
import com.tournamentmanagmentsystem.dto.response.ParticipantResponse;
import com.tournamentmanagmentsystem.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing Tournament Participants.
 * Handles public-facing registration and participant listing.
 */
@RestController
@RequestMapping("/api/v1/participants")
@RequiredArgsConstructor
@Tag(name = "Participants", description = "Operations for tournament registration and roster management")
@SecurityRequirement(name = "bearerAuth")
public class ParticipantController {

    private final ParticipantService participantService;

    /**
     * Registers a participant (Individual or Team) to a tournament.
     *
     * @param request participant data and tournament context
     * @return enrollment confirmation and assigned status (CONFIRMED/WAITLIST)
     */
    @PostMapping("/register")
    @Operation(summary = "Register for a tournament", description = "Registers the current user or a guest as a participant. If the tournament capacity is reached, the status will be set to WAITLIST.", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "Registration is closed or tournament is not found", content = @Content)
    })
    public ResponseEntity<ParticipantResponse> register(@Valid @RequestBody ParticipantRequest request) {
        return ResponseEntity.ok(participantService.register(request));
    }

    /**
     * Lists all participants for a specific tournament.
     *
     * @param tournamentId tournament UUID
     * @return the list of participants
     */
    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Get participant list", description = "Returns all registered participants for the specified tournament, including their status (Confirmed/Waitlist).")
    public ResponseEntity<List<ParticipantResponse>> getByTournament(@PathVariable UUID tournamentId) {
        return ResponseEntity.ok(participantService.getParticipants(tournamentId));
    }
}

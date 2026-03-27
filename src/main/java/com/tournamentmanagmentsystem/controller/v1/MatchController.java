package com.tournamentmanagmentsystem.controller.v1;

import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.dto.request.MatchResultRequest;
import com.tournamentmanagmentsystem.dto.response.MatchResponse;
import com.tournamentmanagmentsystem.service.MatchService;
import com.tournamentmanagmentsystem.service.bracket.BracketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for match management, results, and bracket generation.
 * Secured by RBAC checked against event administrators and referees.
 */
@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Operations for match progression, result logging, and bracket management")
@SecurityRequirement(name = "bearerAuth")
public class MatchController {

    private final BracketService bracketService;
    private final MatchService matchService;

    /**
     * Generates a tournament bracket (initial matches) for an event.
     * Restricted to Tournament Admins.
     *
     * @param eventId the event UUID
     * @param format  the tournament format (SINGLE_ELIMINATION, ROUND_ROBIN)
     * @return list of generated matches
     */
    @PostMapping("/generate-bracket/{eventId}")
    @Operation(summary = "Generate matches/bracket", description = "Initializes the match sequence based on the event format. Restricted to TOURNAMENT_ADMIN of the event.", responses = {
            @ApiResponse(responseCode = "200", description = "Bracket generated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin role required", content = @Content)
    })
    @PreAuthorize("@securityService.isTournamentAdminOfEvent(#eventId)")
    public ResponseEntity<List<MatchResponse>> generate(@PathVariable UUID eventId, @RequestParam FormatType format) {
        return ResponseEntity.ok(bracketService.generateBracket(eventId, format).stream()
                .map(match -> MatchResponse.builder()
                        .id(match.getId())
                        .eventId(match.getEvent() != null ? match.getEvent().getId() : null)
                        .stage(match.getStage())
                        .roundNumber(match.getRoundNumber())
                        .participantAName(match.getParticipantA() != null ? match.getParticipantA().getName() : null)
                        .participantBName(match.getParticipantB() != null ? match.getParticipantB().getName() : null)
                        .status(match.getStatus())
                        .score(match.getScore())
                        .winnerId(match.getWinnerId())
                        .scheduledStart(match.getScheduledStart())
                        .build())
                .collect(Collectors.toList()));
    }

    /**
     * Records the score and winner for a specific match.
     * Restricted to Referees or Admins.
     *
     * @param matchId match UUID
     * @param request result data (score, winner)
     * @return updated match details
     */
    @PatchMapping("/{matchId}/result")
    @Operation(summary = "Update match result", description = "Sets the final score and winner of a match. Restricted to REFEREES or TOURNAMENT_ADMINs.", responses = {
            @ApiResponse(responseCode = "200", description = "Result updated and processed"),
            @ApiResponse(responseCode = "403", description = "Access denied - Referee permissions required", content = @Content)
    })
    @PreAuthorize("@securityService.isRefereeOfMatch(#matchId)")
    public ResponseEntity<MatchResponse> updateResult(
            @PathVariable UUID matchId,
            @Parameter(description = "Score and winner ID") @RequestBody MatchResultRequest request) {
        return ResponseEntity.ok(matchService.updateResult(Objects.requireNonNull(matchId), Objects.requireNonNull(request)));
    }
}

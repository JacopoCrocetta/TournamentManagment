package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Organization;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.domain.enums.TournamentStatus;
import com.tournamentmanagmentsystem.dto.request.TournamentRequest;
import com.tournamentmanagmentsystem.dto.response.TournamentResponse;
import com.tournamentmanagmentsystem.repository.OrganizationRepository;
import com.tournamentmanagmentsystem.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing tournament lifecycles.
 * Handles creation, status transitions, and retrieval of tournaments within
 * organizations.
 */
@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final OrganizationRepository organizationRepository;
    private final AuditService auditService;
    private final ModelMapper modelMapper;

    /**
     * Creates a new tournament in a specific organization.
     * Starts in DRAFT status.
     *
     * @param request data for the new tournament
     * @return TournamentResponse with the created tournament details
     * @throws RuntimeException if the organization is not found
     */
    @Transactional
    public TournamentResponse createTournament(TournamentRequest request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found: " + request.getOrganizationId()));

        Tournament tournament = modelMapper.map(request, Tournament.class);
        if (tournament == null) {
            throw new RuntimeException("Failed to map request to Tournament entity");
        }

        tournament.setOrganization(organization);
        tournament.setStatus(TournamentStatus.DRAFT);

        Tournament savedTournament = tournamentRepository.save(tournament);
        auditService.log("CREATE", "TOURNAMENT", savedTournament.getId(), Map.of("name", savedTournament.getName()));

        return modelMapper.map(savedTournament, TournamentResponse.class);
    }

    /**
     * Retrieves specific tournament details by its ID.
     *
     * @param id tournament UUID
     * @return TournamentResponse
     * @throws RuntimeException if tournament is not found
     */
    @Transactional(readOnly = true)
    public TournamentResponse getTournament(UUID id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found: " + id));
        return modelMapper.map(tournament, TournamentResponse.class);
    }

    /**
     * Lists all tournaments belonging to a specific organization.
     *
     * @param organizationId organization UUID
     * @return list of tournament details
     */
    @Transactional(readOnly = true)
    public List<TournamentResponse> getTournamentsByOrganization(UUID organizationId) {
        return tournamentRepository.findByOrganizationId(organizationId).stream()
                .map(tournament -> modelMapper.map(tournament, TournamentResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Transitions a tournament to a new status.
     * Validates if the transition is logically allowed.
     *
     * @param id        tournament UUID
     * @param newStatus target status
     * @return updated tournament details
     */
    @Transactional
    public TournamentResponse updateStatus(UUID id, TournamentStatus newStatus) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found: " + id));

        validateStatusTransition(tournament.getStatus(), newStatus);

        tournament.setStatus(newStatus);
        Tournament savedTournament = tournamentRepository.save(tournament);

        auditService.log("UPDATE_STATUS", "TOURNAMENT", savedTournament.getId(), Map.of("newStatus", newStatus));

        return modelMapper.map(savedTournament, TournamentResponse.class);
    }

    /**
     * Validates if a tournament can move from the current status to the next one.
     * 
     * @param current current tournament status
     * @param next    target tournament status
     * @throws RuntimeException if transition is invalid
     */
    private void validateStatusTransition(TournamentStatus current, TournamentStatus next) {
        if (current == next) {
            return;
        }

        if (current == TournamentStatus.COMPLETED || current == TournamentStatus.CANCELLED) {
            throw new RuntimeException("Cannot change status of a finished or cancelled tournament (" + current + ")");
        }

        // Add more specific rules as needed:
        // e.g. DRAFT -> REGISTRATION_OPEN -> IN_PROGRESS -> COMPLETED
    }
}

package com.tournamentmanagmentsystem.service;

import org.springframework.lang.NonNull;

import com.tournamentmanagmentsystem.domain.entity.Organization;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.domain.enums.TournamentStatus;
import com.tournamentmanagmentsystem.dto.request.TournamentRequest;
import com.tournamentmanagmentsystem.dto.response.TournamentResponse;
import com.tournamentmanagmentsystem.exception.BusinessException;
import com.tournamentmanagmentsystem.exception.ResourceNotFoundException;
import com.tournamentmanagmentsystem.repository.OrganizationRepository;
import com.tournamentmanagmentsystem.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing tournament lifecycles.
 * Handles creation, status transitions, and retrieval of tournaments within
 * organizations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
    @NonNull
    public TournamentResponse createTournament(@NonNull TournamentRequest request) {
        Organization organization = organizationRepository.findById(Objects.requireNonNull(request.getOrganizationId()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Organization not found: " + request.getOrganizationId()));

        Tournament tournament = modelMapper.map(request, Tournament.class);
        if (tournament == null) {
            throw new BusinessException("Failed to map request to Tournament entity");
        }

        tournament.setOrganization(organization);
        tournament.setStatus(TournamentStatus.DRAFT);

        Tournament savedTournament = tournamentRepository.save(tournament);
        log.info("Tournament '{}' created in organization {}", savedTournament.getName(), organization.getName());
        if (savedTournament.getId() != null) {
            auditService.log("CREATE", "TOURNAMENT", Objects.requireNonNull(savedTournament.getId()),
                    Objects.requireNonNull(Map.of("name", savedTournament.getName())));
        }
        return Objects.requireNonNull(modelMapper.map(savedTournament, TournamentResponse.class),
                "Mapped response must not be null");
    }

    /**
     * Retrieves specific tournament details by its ID.
     *
     * @param id tournament UUID
     * @return TournamentResponse
     * @throws RuntimeException if tournament is not found
     */
    @Transactional(readOnly = true)
    @NonNull
    public TournamentResponse getTournament(@NonNull UUID id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found: " + id));
        return Objects.requireNonNull(modelMapper.map(tournament, TournamentResponse.class),
                "Mapped response must not be null");
    }

    /**
     * Lists all tournaments belonging to a specific organization.
     *
     * @param organizationId organization UUID
     * @return list of tournament details
     */
    @Transactional(readOnly = true)
    @NonNull
    public List<TournamentResponse> getTournamentsByOrganization(@NonNull UUID organizationId) {
        return Objects.requireNonNull(tournamentRepository.findByOrganizationId(organizationId).stream()
                .map(tournament -> modelMapper.map(tournament, TournamentResponse.class))
                .collect(Collectors.toList()), "Mapped response list must not be null");
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
    @NonNull
    public TournamentResponse updateStatus(@NonNull UUID id, @NonNull TournamentStatus newStatus) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found: " + id));

        validateStatusTransition(tournament.getStatus(), newStatus);

        tournament.setStatus(newStatus);
        Tournament savedTournament = tournamentRepository.save(tournament);

        log.info("Tournament {} status updated to {}", savedTournament.getId(), newStatus);

        if (savedTournament.getId() != null) {
            auditService.log("UPDATE_STATUS", "TOURNAMENT", Objects.requireNonNull(savedTournament.getId()),
                    Objects.requireNonNull(Map.of("newStatus", newStatus)));
        }
        return Objects.requireNonNull(modelMapper.map(savedTournament, TournamentResponse.class),
                "Mapped response must not be null");
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
            throw new BusinessException("Cannot change status of a finished or cancelled tournament (" + current + ")");
        }

        // Add more specific rules as needed:
        // e.g. DRAFT -> REGISTRATION_OPEN -> IN_PROGRESS -> COMPLETED
    }
}

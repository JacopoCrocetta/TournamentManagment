package com.tournamentmanagmentsystem.service;

import org.springframework.lang.NonNull;

import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.domain.entity.User;
import com.tournamentmanagmentsystem.domain.enums.ParticipantStatus;
import com.tournamentmanagmentsystem.domain.enums.TournamentStatus;
import com.tournamentmanagmentsystem.dto.request.ParticipantRequest;
import com.tournamentmanagmentsystem.dto.response.ParticipantResponse;
import com.tournamentmanagmentsystem.exception.BusinessException;
import com.tournamentmanagmentsystem.exception.ResourceNotFoundException;
import com.tournamentmanagmentsystem.repository.ParticipantRepository;
import com.tournamentmanagmentsystem.repository.TournamentRepository;
import com.tournamentmanagmentsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for handling participant registrations and player management.
 * Manages confirmed lists and waitlists based on tournament capacity.
 */
@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * Registers a new participant for a tournament.
     * Automatically assigns to WAITLIST if max capacity is reached.
     *
     * @param request registration details
     * @return ParticipantResponse with status and name
     * @throws RuntimeException if registration is closed or tournament not found
     */
    @Transactional
    @NonNull
    public ParticipantResponse register(@NonNull ParticipantRequest request) {
        Tournament tournament = Objects.requireNonNull(fetchTournament(
                Objects.requireNonNull(request.getTournamentId(), "Tournament ID must not be null")), "Tournament must not be null");
        ensureRegistrationIsOpen(tournament);

        ParticipantStatus enrollmentStatus = determineEnrollmentStatus(tournament);

        Participant participant = Objects.requireNonNull(Participant.builder()
                .tournament(tournament)
                .name(request.getName())
                .status(enrollmentStatus)
                .metadata(request.getMetadata())
                .build(), "Participant must not be null");

        associateUserIfExists(participant, request.getUserId());

        Participant savedParticipant = Objects.requireNonNull(participantRepository.save(participant), "Saved participant must not be null");
        ParticipantResponse response = Objects.requireNonNull(modelMapper.map(savedParticipant, ParticipantResponse.class), "Failed to map participant response");
        return response;
    }

    /**
     * Retrieves all participants signed up for a specific tournament.
     *
     * @param tournamentId tournament UUID
     * @return list of participant details
     */
    @Transactional(readOnly = true)
    @NonNull
    public List<ParticipantResponse> getParticipants(@NonNull UUID tournamentId) {
        return Objects.requireNonNull(participantRepository.findByTournamentId(tournamentId).stream()
                .map(participant -> modelMapper.map(participant, ParticipantResponse.class))
                .collect(Collectors.toList()), "Participants list must not be null");
    }

    private Tournament fetchTournament(@NonNull UUID tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found: " + tournamentId));
    }

    private void ensureRegistrationIsOpen(@NonNull Tournament tournament) {
        if (tournament.getStatus() != TournamentStatus.REGISTRATION_OPEN) {
            throw new BusinessException("Registration is currently " + tournament.getStatus() + " and not open");
        }
    }

    private ParticipantStatus determineEnrollmentStatus(@NonNull Tournament tournament) {
        long currentParticipantCount = participantRepository.countByTournamentId(tournament.getId());
        return (currentParticipantCount < tournament.getMaxParticipants())
                ? ParticipantStatus.CONFIRMED
                : ParticipantStatus.WAITLIST;
    }

    private void associateUserIfExists(@NonNull Participant participant, UUID userId) {
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Target user not found for association: " + userId));
            participant.setUser(user);
        }
    }
}

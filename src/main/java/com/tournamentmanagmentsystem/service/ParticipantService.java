package com.tournamentmanagmentsystem.service;

import org.springframework.lang.NonNull;

import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.domain.entity.User;
import com.tournamentmanagmentsystem.domain.enums.ParticipantStatus;
import com.tournamentmanagmentsystem.domain.enums.TournamentStatus;
import com.tournamentmanagmentsystem.dto.request.ParticipantRequest;
import com.tournamentmanagmentsystem.dto.response.ParticipantResponse;
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

import com.tournamentmanagmentsystem.exception.NotFoundException;
import com.tournamentmanagmentsystem.exception.BusinessRuleViolationException;

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
     * @throws NotFoundException if tournament not found
     * @throws BusinessRuleViolationException if registration is closed
     */
    @Transactional
    public ParticipantResponse register(ParticipantRequest request) {
        Tournament tournament = fetchTournament(Objects.requireNonNull(request.getTournamentId()));
        ensureRegistrationIsOpen(tournament);

        ParticipantStatus enrollmentStatus = determineEnrollmentStatus(tournament);

        Participant participant = Participant.builder()
                .tournament(tournament)
                .name(request.getName())
                .type(com.tournamentmanagmentsystem.domain.enums.ParticipantType.PLAYER)
                .status(enrollmentStatus)
                .metadata(request.getMetadata())
                .build();

        associateUserIfExists(participant, request.getUserId());

        Participant savedParticipant = participantRepository.save(Objects.requireNonNull(participant));
        return modelMapper.map(savedParticipant, ParticipantResponse.class);
    }

    /**
     * Retrieves all participants signed up for a specific tournament.
     *
     * @param tournamentId tournament UUID
     * @return list of participant details
     */
    @Transactional(readOnly = true)
    public List<ParticipantResponse> getParticipants(@NonNull UUID tournamentId) {
        return participantRepository.findByTournamentId(Objects.requireNonNull(tournamentId)).stream()
                .map(participant -> modelMapper.map(participant, ParticipantResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Checks in a participant for a tournament.
     *
     * @param participantId UUID of the participant
     * @return updated ParticipantResponse
     * @throws NotFoundException if the participant is not found
     * @throws BusinessRuleViolationException if already checked in
     */
    @Transactional
    public ParticipantResponse checkIn(@NonNull UUID participantId) {
        Participant participant = participantRepository.findById(Objects.requireNonNull(participantId))
                .orElseThrow(() -> new NotFoundException("Participant not found: " + participantId));
        
        if (Boolean.TRUE.equals(participant.getCheckedIn())) {
            throw new BusinessRuleViolationException("Participant already checked in: " + participantId);
        }
        
        participant.setCheckedIn(true);
        Participant savedParticipant = participantRepository.save(Objects.requireNonNull(participant));
        return modelMapper.map(savedParticipant, ParticipantResponse.class);
    }

    private Tournament fetchTournament(@NonNull UUID tournamentId) {
        return tournamentRepository.findById(Objects.requireNonNull(tournamentId))
                .orElseThrow(() -> new NotFoundException("Tournament not found: " + tournamentId));
    }

    private void ensureRegistrationIsOpen(Tournament tournament) {
        if (tournament.getStatus() != TournamentStatus.REGISTRATION_OPEN) {
            throw new BusinessRuleViolationException("Registration is currently " + tournament.getStatus() + " and not open");
        }
    }

    private ParticipantStatus determineEnrollmentStatus(Tournament tournament) {
        long currentParticipantCount = participantRepository.countByTournamentId(Objects.requireNonNull(tournament.getId()));
        return (currentParticipantCount < tournament.getMaxParticipants())
                ? ParticipantStatus.CONFIRMED
                : ParticipantStatus.WAITLIST;
    }

    private void associateUserIfExists(Participant participant, UUID userId) {
        if (userId != null) {
            User user = userRepository.findById(Objects.requireNonNull(userId))
                    .orElseThrow(() -> new NotFoundException("Target user not found for association: " + userId));
            participant.setUser(user);
        }
    }
}

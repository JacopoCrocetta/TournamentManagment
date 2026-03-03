package com.tournamentmanagmentsystem.repository;

import com.tournamentmanagmentsystem.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    long countByTournamentId(UUID tournamentId);

    java.util.List<Participant> findByTournamentId(UUID tournamentId);
}
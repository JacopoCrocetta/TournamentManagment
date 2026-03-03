package com.tournamentmanagmentsystem.repository;

import com.tournamentmanagmentsystem.domain.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, UUID> {
    java.util.List<Tournament> findByOrganizationId(UUID organizationId);
}
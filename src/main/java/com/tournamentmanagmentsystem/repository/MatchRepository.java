package com.tournamentmanagmentsystem.repository;

import com.tournamentmanagmentsystem.domain.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
    java.util.List<Match> findByEventId(UUID eventId);
    Optional<Match> findByEventIdAndStage(UUID eventId, String stage);
}
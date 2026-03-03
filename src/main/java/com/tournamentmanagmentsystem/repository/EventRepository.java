package com.tournamentmanagmentsystem.repository;

import com.tournamentmanagmentsystem.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    java.util.List<Event> findByTournamentId(UUID tournamentId);
}

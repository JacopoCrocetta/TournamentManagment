package com.tournamentmanagmentsystem.repository;

import com.tournamentmanagmentsystem.domain.entity.Standing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StandingRepository extends JpaRepository<Standing, UUID> {
    java.util.Optional<Standing> findByEventIdAndParticipantId(UUID eventId, UUID participantId);

    java.util.List<Standing> findByEventIdOrderByPointsDesc(UUID eventId);
}

package com.tournamentmanagmentsystem.repository;

import com.tournamentmanagmentsystem.domain.entity.Membership;
import com.tournamentmanagmentsystem.domain.entity.Organization;
import com.tournamentmanagmentsystem.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    List<Membership> findByUser(User user);

    List<Membership> findByOrganization(Organization organization);

    @org.springframework.data.jpa.repository.Query("SELECT m FROM Membership m WHERE m.user.id = :userId AND m.organization.id = :orgId")
    java.util.Optional<Membership> findByUserIdAndOrganizationId(UUID userId, UUID orgId);
}

package com.tournamentmanagmentsystem.security;

import org.springframework.lang.NonNull;

import com.tournamentmanagmentsystem.domain.enums.Role;
import com.tournamentmanagmentsystem.repository.MembershipRepository;
import com.tournamentmanagmentsystem.repository.TournamentRepository;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final MembershipRepository membershipRepository;
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;
    private final MatchRepository matchRepository;

    public boolean hasRoleInOrganization(@NonNull UUID organizationId, @NonNull String roleName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return false;
        }

        Role requiredRole = Role.valueOf(roleName);
        UUID userId = userDetails.getId();

        return membershipRepository.findByUserIdAndOrganizationId(userId, organizationId)
                .map(m -> isHigherOrEqual(m.getRole(), requiredRole))
                .orElse(false);
    }

    public boolean hasRoleInTournament(@NonNull UUID tournamentId, @NonNull String roleName) {
        return tournamentRepository.findById(tournamentId)
                .map(t -> hasRoleInOrganization(Objects.requireNonNull(t.getOrganization().getId()), roleName))
                .orElse(false);
    }

    public boolean isTournamentAdminOfEvent(@NonNull UUID eventId) {
        return eventRepository.findById(eventId)
                .map(e -> hasRoleInTournament(Objects.requireNonNull(e.getTournament().getId()), "TOURNAMENT_ADMIN"))
                .orElse(false);
    }

    public boolean isRefereeOfMatch(@NonNull UUID matchId) {
        return matchRepository.findById(matchId)
                .map(m -> hasRoleInTournament(Objects.requireNonNull(m.getEvent().getTournament().getId()), "REFEREE")
                        || hasRoleInTournament(Objects.requireNonNull(m.getEvent().getTournament().getId()), "TOURNAMENT_ADMIN"))
                .orElse(false);
    }

    private boolean isHigherOrEqual(Role userRole, Role requiredRole) {
        if (userRole == Role.SUPER_ADMIN)
            return true;
        if (userRole == Role.ORG_ADMIN && requiredRole != Role.SUPER_ADMIN)
            return true;
        if (userRole == Role.TOURNAMENT_ADMIN && (requiredRole == Role.TOURNAMENT_ADMIN || requiredRole == Role.REFEREE
                || requiredRole == Role.PLAYER || requiredRole == Role.VIEWER))
            return true;
        if (userRole == Role.REFEREE
                && (requiredRole == Role.REFEREE || requiredRole == Role.PLAYER || requiredRole == Role.VIEWER))
            return true;
        if (userRole == Role.PLAYER && (requiredRole == Role.PLAYER || requiredRole == Role.VIEWER))
            return true;
        return userRole == requiredRole;
    }
}

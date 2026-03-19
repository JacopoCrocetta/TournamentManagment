package com.tournamentmanagmentsystem.security;

import com.tournamentmanagmentsystem.domain.entity.Membership;
import com.tournamentmanagmentsystem.domain.entity.Organization;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.domain.enums.Role;
import com.tournamentmanagmentsystem.repository.MembershipRepository;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.TournamentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private MatchRepository matchRepository;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private SecurityService securityService;

    private UUID userId;
    private UUID orgId;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        orgId = UUID.randomUUID();
        userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void hasRoleInOrganization_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        Membership membership = Membership.builder()
                .role(Role.ORG_ADMIN)
                .build();

        when(membershipRepository.findByUserIdAndOrganizationId(Objects.requireNonNull(userId), Objects.requireNonNull(orgId)))
                .thenReturn(Optional.of(membership));

        assertTrue(securityService.hasRoleInOrganization(Objects.requireNonNull(orgId), "TOURNAMENT_ADMIN"));
        assertFalse(securityService.hasRoleInOrganization(Objects.requireNonNull(orgId), "SUPER_ADMIN"));
    }

    @Test
    void hasRoleInTournament_Success() {
        UUID tournamentId = UUID.randomUUID();
        Tournament tournament = Tournament.builder()
                .organization(Organization.builder().id(orgId).build())
                .build();

        when(tournamentRepository.findById(Objects.requireNonNull(tournamentId))).thenReturn(Optional.of(Objects.requireNonNull(tournament)));

        // Mock nested call
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(membershipRepository.findByUserIdAndOrganizationId(Objects.requireNonNull(userId), Objects.requireNonNull(orgId)))
                .thenReturn(Optional.of(Objects.requireNonNull(Membership.builder().role(Role.ORG_ADMIN).build())));

        assertTrue(securityService.hasRoleInTournament(Objects.requireNonNull(tournamentId), "TOURNAMENT_ADMIN"));
    }
}

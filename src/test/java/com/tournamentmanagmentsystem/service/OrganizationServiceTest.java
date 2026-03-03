package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Membership;
import com.tournamentmanagmentsystem.domain.entity.Organization;
import com.tournamentmanagmentsystem.domain.entity.User;
import com.tournamentmanagmentsystem.dto.request.OrganizationRequest;
import com.tournamentmanagmentsystem.dto.response.OrganizationResponse;
import com.tournamentmanagmentsystem.repository.MembershipRepository;
import com.tournamentmanagmentsystem.repository.OrganizationRepository;
import com.tournamentmanagmentsystem.repository.UserRepository;
import com.tournamentmanagmentsystem.security.UserDetailsImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuditService auditService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrganizationService organizationService;

    private OrganizationRequest request;
    private Organization organization;
    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder().id(userId).email("admin@test.com").build();

        request = new OrganizationRequest();
        request.setName("Test Org");
        request.setSlug("test-org");

        organization = Organization.builder()
                .id(UUID.randomUUID())
                .name("Test Org")
                .slug("test-org")
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createOrganization_Success() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(organizationRepository.findBySlug(request.getSlug())).thenReturn(Optional.empty());
        when(modelMapper.map(request, Organization.class)).thenReturn(organization);
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(organization, OrganizationResponse.class)).thenReturn(new OrganizationResponse());

        OrganizationResponse response = organizationService.createOrganization(request);

        assertNotNull(response);
        verify(membershipRepository).save(any(Membership.class));
        verify(auditService).log(eq("CREATE"), eq("ORGANIZATION"), any(), any());
    }

    @Test
    void createOrganization_DuplicateSlug_ThrowsException() {
        when(organizationRepository.findBySlug(request.getSlug())).thenReturn(Optional.of(organization));
        assertThrows(RuntimeException.class, () -> organizationService.createOrganization(request));
    }

    @Test
    void getAllMyOrganizations_Success() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(Objects.requireNonNull(userId));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findById(Objects.requireNonNull(userId))).thenReturn(Optional.of(user));

        Membership membership = Membership.builder().user(user).organization(organization).build();
        when(membershipRepository.findByUser(user)).thenReturn(java.util.List.of(membership));
        when(modelMapper.map(any(), eq(OrganizationResponse.class))).thenReturn(new OrganizationResponse());

        java.util.List<OrganizationResponse> responses = organizationService.getAllMyOrganizations();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(membershipRepository).findByUser(user);
    }
}

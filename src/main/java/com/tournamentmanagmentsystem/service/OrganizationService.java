package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Membership;
import com.tournamentmanagmentsystem.domain.entity.Organization;
import com.tournamentmanagmentsystem.domain.entity.User;
import com.tournamentmanagmentsystem.domain.enums.Role;
import com.tournamentmanagmentsystem.dto.request.OrganizationRequest;
import com.tournamentmanagmentsystem.dto.response.OrganizationResponse;
import com.tournamentmanagmentsystem.repository.MembershipRepository;
import com.tournamentmanagmentsystem.repository.OrganizationRepository;
import com.tournamentmanagmentsystem.repository.UserRepository;
import com.tournamentmanagmentsystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing organizations and their members.
 * Handles the lifecycle of organizations and ensures proper role assignment
 * upon creation.
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final ModelMapper modelMapper;

    /**
     * Creates a new organization and assigns the current user as the administrator.
     *
     * @param request data for the new organization (name, slug, etc.)
     * @return OrganizationResponse containing the created organization's details
     * @throws RuntimeException if the slug is already taken
     */
    @Transactional
    public OrganizationResponse createOrganization(OrganizationRequest request) {
        validateSlugUniqueness(request.getSlug());

        Organization organization = modelMapper.map(request, Organization.class);
        organization = organizationRepository.save(organization);

        User currentUser = getCurrentUser();
        createMembership(currentUser, organization, Role.ORG_ADMIN);

        auditService.log("CREATE", "ORGANIZATION", organization.getId(), Map.of("name", organization.getName()));

        return modelMapper.map(organization, OrganizationResponse.class);
    }

    /**
     * Retrieves an organization by its unique identifier.
     *
     * @param id the UUID of the organization
     * @return OrganizationResponse with details
     * @throws RuntimeException if organization is not found
     */
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganization(UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found: " + id));
        return modelMapper.map(organization, OrganizationResponse.class);
    }

    /**
     * Returns a list of organizations where the current user is a member.
     *
     * @return List of OrganizationResponse
     */
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getAllMyOrganizations() {
        User currentUser = getCurrentUser();
        return membershipRepository.findByUser(currentUser).stream()
                .map(membership -> modelMapper.map(membership.getOrganization(), OrganizationResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing organization's metadata.
     *
     * @param id      the UUID of the organization to update
     * @param request the new data (name, description, settings)
     * @return OrganizationResponse with updated details
     */
    @Transactional
    public OrganizationResponse updateOrganization(UUID id, OrganizationRequest request) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found: " + id));

        organization.setName(request.getName());
        organization.setDescription(request.getDescription());
        organization.setSettings(request.getSettings());

        organization = organizationRepository.save(organization);
        auditService.log("UPDATE", "ORGANIZATION", organization.getId(), Map.of("name", organization.getName()));

        return modelMapper.map(organization, OrganizationResponse.class);
    }

    private void validateSlugUniqueness(String slug) {
        if (organizationRepository.findBySlug(slug).isPresent()) {
            throw new RuntimeException("Slug '" + slug + "' is already in use");
        }
    }

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Current authenticated user session is invalid"));
    }

    private void createMembership(User user, Organization organization, Role role) {
        Membership membership = Membership.builder()
                .user(user)
                .organization(organization)
                .role(role)
                .build();
        membershipRepository.save(membership);
    }
}

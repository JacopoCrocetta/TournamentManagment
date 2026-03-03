package com.tournamentmanagmentsystem.controller.v1;

import com.tournamentmanagmentsystem.dto.request.OrganizationRequest;
import com.tournamentmanagmentsystem.dto.response.OrganizationResponse;
import com.tournamentmanagmentsystem.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Organization management.
 * Provides endpoints for creating, retrieving, and updating organizations.
 * Security is enforced via RBAC checked in @PreAuthorize annotations.
 */
@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@Tag(name = "Organizations", description = "Operations related to Organization lifecycle and core settings")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationController {

    private final OrganizationService organizationService;

    /**
     * Creates a new organization. The caller automatically becomes the ORG_ADMIN.
     *
     * @param request metadata for the new organization
     * @return the created organization
     */
    @PostMapping
    @Operation(summary = "Create an organization", description = "Initializes a new organization. The authenticated user will be assigned the ORG_ADMIN role by default.", responses = {
            @ApiResponse(responseCode = "200", description = "Organization created successfully"),
            @ApiResponse(responseCode = "400", description = "Slug already exists or validation failed", content = @Content)
    })
    public ResponseEntity<OrganizationResponse> create(@Valid @RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.createOrganization(request));
    }

    /**
     * Retrieves all organizations where the current user has a membership.
     *
     * @return list of organizations
     */
    @GetMapping
    @Operation(summary = "List current user's organizations", description = "Returns a list of all organizations where the authenticated user is registered as a member.")
    public ResponseEntity<List<OrganizationResponse>> getAll() {
        return ResponseEntity.ok(organizationService.getAllMyOrganizations());
    }

    /**
     * Retrieves specific organization details if the user has at least VIEWER
     * access.
     *
     * @param id organization UUID
     * @return organization details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get organization by ID", description = "Fetches detailed information about a specific organization. Requires VIEWER role or higher.", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved organization details"),
            @ApiResponse(responseCode = "403", description = "Access denied - insufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Organization not found", content = @Content)
    })
    @PreAuthorize("@securityService.hasRoleInOrganization(#id, 'VIEWER')")
    public ResponseEntity<OrganizationResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(organizationService.getOrganization(id));
    }

    /**
     * Updates an existing organization. Requires ORG_ADMIN permissions.
     *
     * @param id      organization UUID
     * @param request updated metadata
     * @return the updated organization
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update organization", description = "Updates the metadata and settings of an existing organization. Restricted to ORG_ADMIN.", responses = {
            @ApiResponse(responseCode = "200", description = "Organization updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required", content = @Content)
    })
    @PreAuthorize("@securityService.hasRoleInOrganization(#id, 'ORG_ADMIN')")
    public ResponseEntity<OrganizationResponse> update(@PathVariable UUID id,
            @Valid @RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.updateOrganization(id, request));
    }
}

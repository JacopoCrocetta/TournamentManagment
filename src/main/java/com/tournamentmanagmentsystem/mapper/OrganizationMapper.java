package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.domain.entity.Organization;
import com.tournamentmanagmentsystem.dto.response.OrganizationResponse;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {
    public OrganizationResponse toResponse(Organization organization) {
        if (organization == null) return null;
        return OrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .slug(organization.getSlug())
                .description(organization.getDescription())
                .settings(organization.getSettings())
                .createdAt(organization.getCreatedAt())
                .build();
    }
}

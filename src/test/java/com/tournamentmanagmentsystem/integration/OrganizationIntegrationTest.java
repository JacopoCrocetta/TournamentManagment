package com.tournamentmanagmentsystem.integration;

import com.tournamentmanagmentsystem.dto.request.OrganizationRequest;
import com.tournamentmanagmentsystem.dto.request.RegisterRequest;
import com.tournamentmanagmentsystem.dto.response.AuthResponse;
import com.tournamentmanagmentsystem.dto.response.OrganizationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for Organization lifecycle.
 * Verifies creation and retrieval while handling JWT security context.
 */
public class OrganizationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @BeforeEach
    void setup() {
        // Register a user to get a valid token
        String email = "org-test-" + java.util.UUID.randomUUID().toString() + "@example.com";
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .password("password")
                .displayName("Org Tester")
                .build();

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/register",
                registerRequest,
                AuthResponse.class);
        
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Setup failed to register user: " + response.getStatusCode());
        }
        token = Objects.requireNonNull(response.getBody()).getAccessToken();
    }

    @Test
    @DisplayName("Should create and then retrieve an organization")
    void organizationLifecycle() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(token));

        OrganizationRequest createRequest = OrganizationRequest.builder()
                .name("Global Esports")
                .slug("global-esports-" + java.util.UUID.randomUUID().toString().substring(0, 8))
                .description("Professional org")
                .build();

        HttpEntity<OrganizationRequest> entity = new HttpEntity<>(createRequest, headers);

        // 1. CREATE
        ResponseEntity<OrganizationResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/organizations",
                entity,
                OrganizationResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(createResponse.getBody()).getName()).isEqualTo("Global Esports");

        // 2. GET BY ID
        ResponseEntity<OrganizationResponse> getResponse = restTemplate.exchange(
                "/api/v1/organizations/" + Objects.requireNonNull(createResponse.getBody()).getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrganizationResponse.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getName()).isEqualTo("Global Esports");
    }
}

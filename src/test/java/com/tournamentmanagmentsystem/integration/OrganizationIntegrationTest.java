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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for Organization lifecycle.
 * Verifies creation and retrieval while handling JWT security context.
 */
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrganizationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @BeforeEach
    void setup() {
        // Register a user to get a valid token
        String email = "org-test-" + System.currentTimeMillis() + "@example.com";
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .password("password")
                .displayName("Org Tester")
                .build();

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/register",
                registerRequest,
                AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthResponse authBody = response.getBody();
        assertThat(authBody).isNotNull();
        token = Objects.requireNonNull(authBody).getAccessToken();
    }

    @Test
    @DisplayName("Should create and then retrieve an organization")
    void organizationLifecycle() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(token));

        OrganizationRequest createRequest = OrganizationRequest.builder()
                .name("Global Esports")
                .slug("global-esports-" + System.currentTimeMillis())
                .description("Professional org")
                .build();

        HttpEntity<OrganizationRequest> entity = new HttpEntity<>(createRequest, headers);

        // 1. CREATE
        ResponseEntity<OrganizationResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/organizations",
                entity,
                OrganizationResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrganizationResponse createBody = createResponse.getBody();
        assertThat(createBody).isNotNull();
        assertThat(Objects.requireNonNull(createBody).getName()).isEqualTo("Global Esports");

        // 2. GET BY ID
        ResponseEntity<OrganizationResponse> getResponse = restTemplate.exchange(
                "/api/v1/organizations/" + Objects.requireNonNull(createBody).getId(),
                org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(headers),
                OrganizationResponse.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrganizationResponse getBody = getResponse.getBody();
        assertThat(getBody).isNotNull();
        assertThat(Objects.requireNonNull(getBody).getName()).isEqualTo("Global Esports");
    }
}

package com.tournamentmanagmentsystem.integration;

import com.tournamentmanagmentsystem.dto.request.AuthRequest;
import com.tournamentmanagmentsystem.dto.request.RegisterRequest;
import com.tournamentmanagmentsystem.dto.response.AuthResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end integration test for the Authentication flow.
 * Verifies that registration and login work against a real PostgreSQL instance.
 */
public class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should successfully register and then login a new user")
    void fullAuthCycle() {
        // 1. REGISTER
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("it-test@example.com")
                .password("StrongPass123!")
                .displayName("IT Tester")
                .build();

        ResponseEntity<AuthResponse> registerResponse = restTemplate.postForEntity(
                "/api/v1/auth/register",
                registerRequest,
                AuthResponse.class);

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthResponse registerBody = Objects.requireNonNull(registerResponse.getBody());
        assertThat(registerBody.getEmail()).isEqualTo("it-test@example.com");
        assertThat(registerBody.getAccessToken()).isNotBlank();

        // 2. LOGIN
        AuthRequest loginRequest = new AuthRequest("it-test@example.com", "StrongPass123!");

        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                "/api/v1/auth/login",
                loginRequest,
                AuthResponse.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthResponse loginBody = Objects.requireNonNull(loginResponse.getBody());
        assertThat(loginBody.getAccessToken()).isNotBlank();
        assertThat(loginBody.getRefreshToken()).isNotBlank();
    }

    @Test
    @DisplayName("Should fail to register with an existing email")
    void duplicateRegistration() {
        RegisterRequest request = RegisterRequest.builder()
                .email("duplicate@example.com")
                .password("password123")
                .displayName("First")
                .build();

        restTemplate.postForEntity("/api/v1/auth/register", request, AuthResponse.class);

        // Try again
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/register",
                request,
                AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        // Note: AuthService throws ConflictException (or similar), which maps to 409.
    }
}

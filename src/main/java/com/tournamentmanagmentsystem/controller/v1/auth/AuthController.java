package com.tournamentmanagmentsystem.controller.v1.auth;

import com.tournamentmanagmentsystem.dto.request.AuthRequest;
import com.tournamentmanagmentsystem.dto.request.RegisterRequest;
import com.tournamentmanagmentsystem.dto.response.AuthResponse;
import com.tournamentmanagmentsystem.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related requests such as registration
 * and login.
 * All endpoints are prefixed with /api/v1/auth.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for managing user accounts and security sessions")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint to register a new user in the system.
     *
     * @param request the registration details
     * @return ResponseEntity with AuthResponse containing tokens and user
     *         information
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns the initial set of access and refresh tokens.", responses = {
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Email already in use or invalid input data", content = @Content)
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint to authenticate an existing user.
     *
     * @param request the login credentials
     * @return ResponseEntity with AuthResponse containing fresh tokens
     */
    @PostMapping("/login")
    @Operation(summary = "Login / Authenticate user", description = "Validates the user's credentials and returns a new session (access and refresh tokens).", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}

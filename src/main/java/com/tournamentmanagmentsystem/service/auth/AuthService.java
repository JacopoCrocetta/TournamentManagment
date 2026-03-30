package com.tournamentmanagmentsystem.service.auth;

import org.springframework.lang.NonNull;

import com.tournamentmanagmentsystem.domain.entity.User;
import com.tournamentmanagmentsystem.domain.enums.UserStatus;
import com.tournamentmanagmentsystem.dto.request.AuthRequest;
import com.tournamentmanagmentsystem.dto.request.RegisterRequest;
import com.tournamentmanagmentsystem.dto.response.AuthResponse;
import com.tournamentmanagmentsystem.exception.ConflictException;
import com.tournamentmanagmentsystem.exception.ResourceNotFoundException;
import com.tournamentmanagmentsystem.repository.UserRepository;
import com.tournamentmanagmentsystem.security.JwtService;
import com.tournamentmanagmentsystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Service responsible for user authentication and registration operations.
 * Handles JWT token generation and password encoding.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        /**
         * Registers a new user in the system.
         *
         * @param request the registration details including email, password, and
         *                display name
         * @return AuthResponse containing access and refresh tokens
         * @throws ConflictException if the email already exists
         */
        @Transactional
        @NonNull
        public AuthResponse register(@NonNull RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        log.warn("Registration attempt failed: email {} already exists", request.getEmail());
                        throw new ConflictException("Account with this email already exists");
                }

                User user = Objects.requireNonNull(User.builder()
                                .displayName(request.getDisplayName())
                                .email(request.getEmail())
                                .passwordHash(passwordEncoder.encode(request.getPassword()))
                                .status(UserStatus.ACTIVE)
                                .build(), "User entity must not be null");

                User savedUser = Objects.requireNonNull(userRepository.save(user), "Saved user must not be null");
                log.info("New user registered successfully: {}", savedUser.getEmail());
                return createAuthResponse(savedUser);
        }

        /**
         * Authenticates a user and generates tokens.
         *
         * @param request the login credentials (email and password)
         * @return AuthResponse containing access and refresh tokens
         * @throws ResourceNotFoundException if user is not found after authentication
         */
        @NonNull
        public AuthResponse login(@NonNull AuthRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User not found after successful authentication"));

                log.info("User {} logged in successfully", user.getEmail());
                return createAuthResponse(user);
        }

        /**
         * Helper method to generate AuthResponse from a User entity.
         * 
         * @param user the user entity
         * @return AuthResponse with JWT tokens
         */
        @NonNull
        private AuthResponse createAuthResponse(@NonNull User user) {
                UserDetailsImpl userDetails = UserDetailsImpl.build(user);
                String accessToken = jwtService.generateToken(userDetails);
                String refreshToken = jwtService.generateRefreshToken(userDetails);

                AuthResponse response = AuthResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .email(user.getEmail())
                                .displayName(user.getDisplayName())
                                .build();

                return Objects.requireNonNull(response, "AuthResponse must not be null");
        }
}

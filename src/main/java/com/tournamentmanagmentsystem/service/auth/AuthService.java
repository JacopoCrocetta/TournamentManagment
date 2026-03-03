package com.tournamentmanagmentsystem.service.auth;

import com.tournamentmanagmentsystem.domain.entity.User;
import com.tournamentmanagmentsystem.domain.enums.UserStatus;
import com.tournamentmanagmentsystem.dto.request.AuthRequest;
import com.tournamentmanagmentsystem.dto.request.RegisterRequest;
import com.tournamentmanagmentsystem.dto.response.AuthResponse;
import com.tournamentmanagmentsystem.repository.UserRepository;
import com.tournamentmanagmentsystem.security.JwtService;
import com.tournamentmanagmentsystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for user authentication and registration operations.
 * Handles JWT token generation and password encoding.
 */
@Service
@RequiredArgsConstructor
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
         * @throws RuntimeException if the email already exists
         */
        @Transactional
        public AuthResponse register(RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new RuntimeException("Account with this email already exists");
                }

                User user = User.builder()
                                .displayName(request.getDisplayName())
                                .email(request.getEmail())
                                .passwordHash(passwordEncoder.encode(request.getPassword()))
                                .status(UserStatus.ACTIVE)
                                .build();

                User savedUser = userRepository.save(user);
                return createAuthResponse(savedUser);
        }

        /**
         * Authenticates a user and generates tokens.
         *
         * @param request the login credentials (email and password)
         * @return AuthResponse containing access and refresh tokens
         */
        public AuthResponse login(AuthRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException(
                                                "User not found after successful authentication"));

                return createAuthResponse(user);
        }

        /**
         * Helper method to generate AuthResponse from a User entity.
         * 
         * @param user the user entity
         * @return AuthResponse with JWT tokens
         */
        private AuthResponse createAuthResponse(User user) {
                UserDetailsImpl userDetails = UserDetailsImpl.build(user);
                String accessToken = jwtService.generateToken(userDetails);
                String refreshToken = jwtService.generateRefreshToken(userDetails);

                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .email(user.getEmail())
                                .displayName(user.getDisplayName())
                                .build();
        }
}

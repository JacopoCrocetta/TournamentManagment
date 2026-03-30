package com.tournamentmanagmentsystem.service.auth;

import com.tournamentmanagmentsystem.domain.entity.User;
import com.tournamentmanagmentsystem.dto.request.AuthRequest;
import com.tournamentmanagmentsystem.dto.request.RegisterRequest;
import com.tournamentmanagmentsystem.dto.response.AuthResponse;
import com.tournamentmanagmentsystem.exception.ConflictException;
import com.tournamentmanagmentsystem.repository.UserRepository;
import com.tournamentmanagmentsystem.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setDisplayName("Test User");

        authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");

        user = User.builder()
                .email("test@example.com")
                .passwordHash("encodedPassword")
                .displayName("Test User")
                .build();
    }

    @Test
    void register_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(Objects.requireNonNull(any(User.class)))).thenReturn(Objects.requireNonNull(user));
        when(jwtService.generateToken(any())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any())).thenReturn("refreshToken");

        AuthResponse response = authService.register(Objects.requireNonNull(registerRequest));

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("accessToken", response.getAccessToken());
        verify(userRepository, times(1)).save(Objects.requireNonNull(any(User.class)));
    }

    @Test
    void register_AlreadyExists_ThrowsException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(Objects.requireNonNull(registerRequest)));
        verify(userRepository, never()).save(Objects.requireNonNull(any(User.class)));
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(Objects.requireNonNull(user)));
        when(jwtService.generateToken(any())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any())).thenReturn("refreshToken");

        AuthResponse response = authService.login(Objects.requireNonNull(authRequest));

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}

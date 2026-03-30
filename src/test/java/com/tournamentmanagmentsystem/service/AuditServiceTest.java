package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.AuditLog;
import com.tournamentmanagmentsystem.repository.AuditLogRepository;
import com.tournamentmanagmentsystem.security.UserDetailsImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuditService auditService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void log_WithUser_Success() {
        UUID userId = UUID.randomUUID();
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        auditService.log("CREATE", "TOURNAMENT", java.util.Objects.requireNonNull(UUID.randomUUID()),
                java.util.Objects.requireNonNull(Map.<String, Object>of("name", "Test")));

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());
        assertEquals(userId, captor.getValue().getUserId());
        assertEquals("CREATE", captor.getValue().getAction());
    }

    @Test
    void log_SystemAction_Success() {
        when(securityContext.getAuthentication()).thenReturn(null);

        auditService.log("SYSTEM_EVENT", "SYSTEM", java.util.Objects.requireNonNull(UUID.randomUUID()),
                java.util.Objects.requireNonNull(Map.<String, Object>of()));

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());
        assertEquals(null, captor.getValue().getUserId());
    }
}

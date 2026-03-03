package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.AuditLog;
import com.tournamentmanagmentsystem.repository.AuditLogRepository;
import com.tournamentmanagmentsystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * Service for asynchronous auditing of system actions.
 * Records user-initiated and system actions into the persistent audit log.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Records an audit entry in the background.
     * Automatically captures the current authenticated user ID.
     *
     * @param action     the performed operation (e.g., CREATE, UPDATE_RESULT)
     * @param entityType the target entity type (e.g., ORGANIZATION, MATCH)
     * @param entityId   the UUID of the target entity
     * @param payload    additional metadata about the action (e.g., changed fields)
     */
    @Async
    public void log(String action, String entityType, UUID entityId, Map<String, Object> payload) {
        UUID userId = getCurrentUserId();

        AuditLog auditLog = AuditLog.builder()
                .userId(userId)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .payload(payload)
                .build();

        auditLogRepository.save(auditLog);
        log.info("Audit log saved: {} on {} ID: {}. Triggered by user: {}", action, entityType, entityId, userId);
    }

    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return null; // Indicates a system-level or unauthenticated action
    }
}

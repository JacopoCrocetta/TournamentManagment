package com.tournamentmanagmentsystem.controller.ws;

import com.tournamentmanagmentsystem.dto.response.MatchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service to handle real-time notifications via WebSockets.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Notifies all subscribers of a match update.
     */
    public void notifyMatchUpdate(UUID eventId, @org.springframework.lang.NonNull MatchResponse match) {
        String destination = "/topic/event/" + eventId + "/matches";
        messagingTemplate.convertAndSend(destination, match);
        log.debug("WebSocket notification sent to {}", destination);
    }

    /**
     * Notifies all subscribers of a bracket update.
     */
    public void notifyBracketUpdate(UUID eventId) {
        String destination = "/topic/event/" + eventId + "/bracket";
        messagingTemplate.convertAndSend(destination, "REFRESH");
        log.debug("Bracket refresh request sent to {}", destination);
    }
}

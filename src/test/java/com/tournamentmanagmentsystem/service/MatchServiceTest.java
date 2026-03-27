package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.dto.request.MatchResultRequest;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import com.tournamentmanagmentsystem.mapper.MatchMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.lang.NonNull;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private com.tournamentmanagmentsystem.repository.ParticipantRepository participantRepository;
    @Mock
    private AuditService auditService;
    @Mock
    private StandingService standingService;
    @Mock
    private com.tournamentmanagmentsystem.controller.ws.NotificationService notificationService;
    @Mock
    private MatchMapper matchMapper;

    @InjectMocks
    private MatchService matchService;

    private UUID matchId;
    private Match match;
    private MatchResultRequest request;
    private Participant pA;
    private Participant pB;

    @BeforeEach
    void setUp() {
        matchId = UUID.randomUUID();
        pA = Participant.builder().id(UUID.randomUUID()).name("Player A").build();
        pB = Participant.builder().id(UUID.randomUUID()).name("Player B").build();

        match = Match.builder()
                .id(matchId)
                .event(Event.builder().id(UUID.randomUUID()).build())
                .participantA(pA)
                .participantB(pB)
                .status(MatchStatus.PENDING)
                .score(new HashMap<>())
                .build();

        request = new MatchResultRequest();
        request.setWinnerId(pA.getId());
        request.setScore(Map.of(pA.getId().toString(), 2, pB.getId().toString(), 1));
    }

    @Test
    void updateResult_Success_WithAdvancement() {
        Match nextMatch = Match.builder().id(UUID.randomUUID()).build();
        match.setNextMatch(nextMatch);
        match.setPositionInNextMatch(0);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(participantRepository.findById(pA.getId())).thenReturn(Optional.of(pA));
        when(matchMapper.toResponse(any())).thenReturn(new com.tournamentmanagmentsystem.dto.response.MatchResponse());

        matchService.updateResult(matchId, request);

        assertEquals(MatchStatus.FINISHED, match.getStatus());
        assertEquals(pA.getId(), match.getWinnerId());
        assertEquals(pA, nextMatch.getParticipantA());
        verify(matchRepository).save(nextMatch);
        verify(notificationService).notifyMatchUpdate(any(), any());
    }
}

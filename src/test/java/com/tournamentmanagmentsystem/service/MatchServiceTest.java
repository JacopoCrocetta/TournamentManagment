package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.dto.request.MatchResultRequest;
import com.tournamentmanagmentsystem.dto.response.MatchResponse;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import com.tournamentmanagmentsystem.service.bracket.SingleEliminationEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private AuditService auditService;
    @Mock
    private StandingService standingService;
    @Mock
    private SingleEliminationEngine singleEliminationEngine;
    @Mock
    private ModelMapper modelMapper;

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
        pA = Participant.builder().id(UUID.randomUUID()).build();
        pB = Participant.builder().id(UUID.randomUUID()).build();

        match = Match.builder()
                .id(matchId)
                .event(new Event())
                .participantA(pA)
                .participantB(pB)
                .status(MatchStatus.PENDING)
                .score(new HashMap<>())
                .build();

        request = new MatchResultRequest();
        request.setWinnerId(pA.getId());
        request.setScore(Map.of("A", 2, "B", 1));
    }

    @Test
    void updateResult_Success() {
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(modelMapper.map(any(), eq(MatchResponse.class))).thenReturn(new MatchResponse());

        matchService.updateResult(matchId, request);

        assertEquals(MatchStatus.FINISHED, match.getStatus());
        assertEquals(pA.getId(), match.getWinnerId());
        verify(standingService, times(2)).updateStanding(any(Event.class), any(Participant.class), anyInt(), any());
        verify(singleEliminationEngine, times(1)).advanceWinner(match);
    }
}

package com.tournamentmanagmentsystem.service.bracket;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.repository.ParticipantRepository;
import com.tournamentmanagmentsystem.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BracketServiceTest {

    @Mock
    private SingleEliminationEngine singleEliminationEngine;
    @Mock
    private RoundRobinEngine roundRobinEngine;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private AuditService auditService;

    @InjectMocks
    private BracketService bracketService;

    private UUID eventId;
    private List<Participant> participants;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        participants = List.of(Participant.builder().id(UUID.randomUUID()).build());
        when(participantRepository.findByTournamentId(eventId)).thenReturn(participants);
    }

    @Test
    void generateBracket_SingleElimination_Success() {
        List<Match> expectedMatches = List.of(new Match());
        when(singleEliminationEngine.generateInitialMatches(eventId, participants)).thenReturn(expectedMatches);

        List<Match> results = bracketService.generateBracket(eventId, FormatType.SINGLE_ELIMINATION);

        assertEquals(expectedMatches, results);
        verify(singleEliminationEngine).generateInitialMatches(eventId, participants);
        verify(roundRobinEngine, never()).generateInitialMatches(any(), any());
        verify(auditService).log(eq("GENERATE_BRACKET"), eq("EVENT"), eq(eventId), anyMap());
    }

    @Test
    void generateBracket_RoundRobin_Success() {
        List<Match> expectedMatches = List.of(new Match());
        when(roundRobinEngine.generateInitialMatches(eventId, participants)).thenReturn(expectedMatches);

        List<Match> results = bracketService.generateBracket(eventId, FormatType.ROUND_ROBIN);

        assertEquals(expectedMatches, results);
        verify(roundRobinEngine).generateInitialMatches(eventId, participants);
        verify(singleEliminationEngine, never()).generateInitialMatches(any(), any());
    }

    @Test
    void generateBracket_UnsupportedFormat_ThrowsException() {
        assertThrows(UnsupportedOperationException.class,
                () -> bracketService.generateBracket(eventId, FormatType.DOUBLE_ELIMINATION));
    }
}

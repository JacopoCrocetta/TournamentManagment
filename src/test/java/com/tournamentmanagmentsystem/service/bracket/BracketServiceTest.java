package com.tournamentmanagmentsystem.service.bracket;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.exception.BusinessException;
import com.tournamentmanagmentsystem.repository.ParticipantRepository;
import com.tournamentmanagmentsystem.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;
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

    @NonNull
    private UUID eventId = Objects.requireNonNull(UUID.randomUUID());
    private List<Participant> participants;

    @BeforeEach
    void setUp() {
        participants = List.of(Participant.builder().id(UUID.randomUUID()).build());
        when(participantRepository.findByTournamentId(eventId)).thenReturn(participants);
    }

    @Test
    @SuppressWarnings("null")
    void generateBracket_SingleElimination_Success() {
        List<Match> expectedMatches = List.of(new Match());
        when(singleEliminationEngine.generateInitialMatches(Objects.requireNonNull(eventId),
                Objects.requireNonNull(participants))).thenReturn(expectedMatches);

        List<Match> results = bracketService.generateBracket(eventId, FormatType.SINGLE_ELIMINATION);

        assertEquals(expectedMatches, results);
        verify(singleEliminationEngine).generateInitialMatches(Objects.requireNonNull(eventId),
                Objects.requireNonNull(participants));
        verify(roundRobinEngine, never()).generateInitialMatches(Objects.requireNonNull(eventId), Objects.requireNonNull(participants));
        verify(auditService).log(eq("GENERATE_BRACKET"), eq("EVENT"), eq(Objects.requireNonNull(eventId)), anyMap());
    }

    @Test
    void generateBracket_RoundRobin_Success() {
        List<Match> expectedMatches = List.of(new Match());
        when(roundRobinEngine.generateInitialMatches(Objects.requireNonNull(eventId),
                Objects.requireNonNull(participants))).thenReturn(expectedMatches);

        List<Match> results = bracketService.generateBracket(eventId, FormatType.ROUND_ROBIN);

        assertEquals(expectedMatches, results);
        verify(roundRobinEngine).generateInitialMatches(Objects.requireNonNull(eventId),
                Objects.requireNonNull(participants));
        verify(singleEliminationEngine, never()).generateInitialMatches(Objects.requireNonNull(eventId), Objects.requireNonNull(participants));
    }

    @Test
    void generateBracket_UnsupportedFormat_ThrowsException() {
        assertThrows(BusinessException.class,
                () -> bracketService.generateBracket(Objects.requireNonNull(eventId), FormatType.DOUBLE_ELIMINATION));
    }
}

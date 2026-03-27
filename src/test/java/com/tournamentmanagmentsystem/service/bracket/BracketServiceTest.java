package com.tournamentmanagmentsystem.service.bracket;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import com.tournamentmanagmentsystem.repository.ParticipantRepository;
import com.tournamentmanagmentsystem.service.AuditService;
import com.tournamentmanagmentsystem.strategy.MatchmakingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BracketServiceTest {

    @Mock
    private MatchmakingStrategy singleEliminationStrategy;
    @Mock
    private MatchmakingStrategy roundRobinStrategy;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private AuditService auditService;

    private BracketService bracketService;

    private UUID eventId;
    private UUID tournamentId;
    private Tournament tournament;
    private Event event;
    private List<Participant> participants;

    @BeforeEach
    void setUp() {
        tournamentId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        
        tournament = new Tournament();
        tournament.setId(tournamentId);
        
        event = new Event();
        event.setId(eventId);
        event.setTournament(tournament);

        participants = List.of(Participant.builder().id(UUID.randomUUID()).checkedIn(true).build());
        
        Map<String, MatchmakingStrategy> strategies = Map.of(
            FormatType.SINGLE_ELIMINATION.name(), singleEliminationStrategy,
            FormatType.ROUND_ROBIN.name(), roundRobinStrategy
        );
        
        bracketService = new BracketService(strategies, participantRepository, eventRepository, matchRepository, auditService);

        lenient().when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        lenient().when(participantRepository.findByTournamentId(any())).thenReturn(participants);
    }

    @Test
    void generateBracket_SingleElimination_Success() {
        List<Match> expectedMatches = List.of(new Match());
        when(singleEliminationStrategy.generateMatches(any(), eq(participants))).thenReturn(expectedMatches);
        when(matchRepository.saveAll(any())).thenReturn(expectedMatches);

        List<Match> results = bracketService.generateBracket(eventId, FormatType.SINGLE_ELIMINATION);

        assertEquals(expectedMatches, results);
        verify(singleEliminationStrategy).generateMatches(any(), eq(participants));
        verify(auditService).log(eq("GENERATE_BRACKET"), eq("EVENT"), eq(eventId), anyMap());
    }

    @Test
    void generateBracket_RoundRobin_Success() {
        List<Match> expectedMatches = List.of(new Match());
        when(roundRobinStrategy.generateMatches(any(), eq(participants))).thenReturn(expectedMatches);
        when(matchRepository.saveAll(any())).thenReturn(expectedMatches);

        List<Match> results = bracketService.generateBracket(eventId, FormatType.ROUND_ROBIN);

        assertEquals(expectedMatches, results);
        verify(roundRobinStrategy).generateMatches(any(), eq(participants));
    }

    @Test
    void generateBracket_UnsupportedFormat_ThrowsException() {
        assertThrows(UnsupportedOperationException.class,
                () -> bracketService.generateBracket(eventId, FormatType.SWISS));
    }
}

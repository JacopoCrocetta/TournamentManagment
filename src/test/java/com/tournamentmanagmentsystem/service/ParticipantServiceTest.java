package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.domain.enums.ParticipantStatus;
import com.tournamentmanagmentsystem.domain.enums.TournamentStatus;
import com.tournamentmanagmentsystem.dto.request.ParticipantRequest;
import com.tournamentmanagmentsystem.dto.response.ParticipantResponse;
import com.tournamentmanagmentsystem.repository.ParticipantRepository;
import com.tournamentmanagmentsystem.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ParticipantService participantService;

    private ParticipantRequest request;
    private Tournament tournament;
    private Participant participant;
    private UUID tournamentId;

    @BeforeEach
    void setUp() {
        tournamentId = UUID.randomUUID();
        tournament = Tournament.builder()
                .id(tournamentId)
                .maxParticipants(2)
                .build();

        request = new ParticipantRequest();
        request.setTournamentId(tournamentId);
        request.setName("Player 1");

        participant = Participant.builder()
                .id(UUID.randomUUID())
                .name("Player 1")
                .tournament(tournament)
                .build();
    }

    @Test
    void register_Confirmed_Success() {
        tournament.setStatus(TournamentStatus.REGISTRATION_OPEN);
        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(participantRepository.countByTournamentId(tournamentId)).thenReturn(1L);
        when(participantRepository.save(any(Participant.class))).thenAnswer(i -> i.getArguments()[0]);
        when(modelMapper.map(any(Participant.class), eq(ParticipantResponse.class)))
                .thenReturn(new ParticipantResponse());

        participantService.register(request);

        org.mockito.ArgumentCaptor<Participant> captor = org.mockito.ArgumentCaptor.forClass(Participant.class);
        verify(participantRepository).save(captor.capture());
        assertEquals(ParticipantStatus.CONFIRMED, captor.getValue().getStatus());
    }

    @Test
    void register_Waitlist_Success() {
        tournament.setStatus(TournamentStatus.REGISTRATION_OPEN);
        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(participantRepository.countByTournamentId(tournamentId)).thenReturn(2L);
        when(participantRepository.save(any(Participant.class))).thenAnswer(i -> i.getArguments()[0]);
        when(modelMapper.map(any(Participant.class), eq(ParticipantResponse.class)))
                .thenReturn(new ParticipantResponse());

        participantService.register(request);

        org.mockito.ArgumentCaptor<Participant> captor = org.mockito.ArgumentCaptor.forClass(Participant.class);
        verify(participantRepository).save(captor.capture());
        assertEquals(ParticipantStatus.WAITLIST, captor.getValue().getStatus());
    }
}

package com.tournamentmanagmentsystem.service;

import com.tournamentmanagmentsystem.domain.entity.Organization;
import com.tournamentmanagmentsystem.domain.entity.Tournament;
import com.tournamentmanagmentsystem.domain.enums.TournamentStatus;
import com.tournamentmanagmentsystem.dto.request.TournamentRequest;
import com.tournamentmanagmentsystem.dto.response.TournamentResponse;
import com.tournamentmanagmentsystem.repository.OrganizationRepository;
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

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private AuditService auditService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TournamentService tournamentService;

    private TournamentRequest request;
    private Tournament tournament;
    private Organization organization;
    private UUID orgId;

    @BeforeEach
    void setUp() {
        orgId = UUID.randomUUID();
        organization = Organization.builder().id(orgId).name("Test Org").build();

        request = new TournamentRequest();
        request.setName("Test Tournament");
        request.setOrganizationId(orgId);

        tournament = Tournament.builder()
                .id(UUID.randomUUID())
                .name("Test Tournament")
                .organization(organization)
                .status(TournamentStatus.DRAFT)
                .build();
    }

    @Test
    void createTournament_Success() {
        when(organizationRepository.findById(orgId)).thenReturn(Optional.of(organization));
        when(modelMapper.map(request, Tournament.class)).thenReturn(tournament);
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);
        when(modelMapper.map(tournament, TournamentResponse.class)).thenReturn(new TournamentResponse());

        TournamentResponse response = tournamentService.createTournament(request);

        assertNotNull(response);
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
        verify(auditService, times(1)).log(eq("CREATE"), eq("TOURNAMENT"), any(), any());
    }

    @Test
    void createTournament_OrganizationNotFound_ThrowsException() {
        when(organizationRepository.findById(orgId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> tournamentService.createTournament(request));
        verify(tournamentRepository, never()).save(any());
    }
}

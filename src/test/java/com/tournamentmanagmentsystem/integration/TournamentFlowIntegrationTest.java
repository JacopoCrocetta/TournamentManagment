package com.tournamentmanagmentsystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tournamentmanagmentsystem.domain.enums.FormatType;
import com.tournamentmanagmentsystem.domain.enums.SeedingPolicy;
import com.tournamentmanagmentsystem.domain.enums.TournamentStatus;
import com.tournamentmanagmentsystem.domain.enums.TournamentVisibility;
import com.tournamentmanagmentsystem.dto.request.*;
import com.tournamentmanagmentsystem.dto.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.annotation.DirtiesContext;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TournamentFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Register a user via API to get auth token
        RegisterRequest registerReq = RegisterRequest.builder()
                .email("admin-flow@test.com" + UUID.randomUUID()) // ensure unique
                .password("password123")
                .displayName("Flow Admin")
                .build();

        String jsonReq = objectMapper.writeValueAsString(registerReq);
        MvcResult regResult = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(jsonReq)))
                .andExpect(status().isOk())
                .andReturn();

        AuthResponse authRes = objectMapper.readValue(regResult.getResponse().getContentAsString(), AuthResponse.class);
        adminToken = "Bearer " + authRes.getAccessToken();
    }

    @Test
    void testEndToEndTournamentFlow() throws Exception {
        // 1. Create Organization
        OrganizationRequest orgReq = OrganizationRequest.builder()
                .slug("test-org-slug-" + UUID.randomUUID())
                .name("Test Org")
                .description("Desc")
                .build();
                
        String orgJson = objectMapper.writeValueAsString(orgReq);
        MvcResult orgResult = mockMvc.perform(post("/api/v1/organizations")
                .header("Authorization", adminToken)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(orgJson)))
                .andExpect(status().isOk())
                .andReturn();
        OrganizationResponse orgRes = objectMapper.readValue(orgResult.getResponse().getContentAsString(), OrganizationResponse.class);
        UUID orgId = orgRes.getId();

        // 2. Create Tournament
        TournamentRequest tourReq = TournamentRequest.builder()
                .organizationId(orgId)
                .name("Epic Tournament")
                .description("desc")
                .sportType("Tennis")
                .format(FormatType.SINGLE_ELIMINATION)
                .visibility(TournamentVisibility.PUBLIC)
                .seedingPolicy(SeedingPolicy.RANDOM)
                .maxParticipants(8)
                .startDate(LocalDateTime.now().plusDays(1))
                .build();
                
        String tourJson = objectMapper.writeValueAsString(tourReq);
        MvcResult tourResult = mockMvc.perform(post("/api/v1/tournaments")
                .header("Authorization", adminToken)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(tourJson)))
                .andExpect(status().isOk())
                .andReturn();
        TournamentResponse tourRes = objectMapper.readValue(tourResult.getResponse().getContentAsString(), TournamentResponse.class);
        UUID tournamentId = tourRes.getId();

        // 2.1 Open Registration
        mockMvc.perform(patch("/api/v1/tournaments/" + tournamentId + "/status?status=" + TournamentStatus.REGISTRATION_OPEN)
                .header("Authorization", adminToken))
                .andExpect(status().isOk());

        // 3. Register Participants
        UUID[] pIds = new UUID[4];
        for (int i = 0; i < 4; i++) {
            ParticipantRequest pReq = ParticipantRequest.builder()
                    .tournamentId(tournamentId)
                    .name("Player " + i)
                    .build();
                    
            String pJson = objectMapper.writeValueAsString(pReq);
            MvcResult pResult = mockMvc.perform(post("/api/v1/participants/register")
                    .header("Authorization", adminToken)
                    .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                    .content(Objects.requireNonNull(pJson)))
                    .andExpect(status().isOk())
                    .andReturn();
            ParticipantResponse pRes = objectMapper.readValue(pResult.getResponse().getContentAsString(), ParticipantResponse.class);
            pIds[i] = pRes.getId();

            // 4. Check-in Participant
            mockMvc.perform(put("/api/v1/participants/" + pIds[i] + "/checkin")
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk());
        }

        // 5. Update Tournament Status
        mockMvc.perform(patch("/api/v1/tournaments/" + tournamentId + "/status?status=" + TournamentStatus.REGISTRATION_CLOSED)
                .header("Authorization", adminToken))
                .andExpect(status().isOk());

        // 6. Create Event (Single Elimination)
        EventRequest eventReq = EventRequest.builder()
                .tournamentId(tournamentId)
                .name("Main Event")
                .formatType(FormatType.SINGLE_ELIMINATION)
                .seedingPolicy(SeedingPolicy.RANDOM)
                .build();
                
        String eventJson = objectMapper.writeValueAsString(eventReq);
        MvcResult eventResult = mockMvc.perform(post("/api/v1/events")
                .header("Authorization", adminToken)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(eventJson)))
                .andExpect(status().isOk())
                .andReturn();
        EventResponse eventRes = objectMapper.readValue(eventResult.getResponse().getContentAsString(), EventResponse.class);
        UUID eventId = eventRes.getId();

        // 7. Generate Bracket
        MvcResult bracketResult = mockMvc.perform(post("/api/v1/matches/generate-bracket/" + eventId + "?format=SINGLE_ELIMINATION")
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        MatchResponse[] matches = objectMapper.readValue(bracketResult.getResponse().getContentAsString(), MatchResponse[].class);
        
        assertThat(matches).isNotEmpty();
        
        // Find a first round match 
        UUID firstRoundMatchId = null;
        for(MatchResponse r : matches) {
            String stage = r.getStage();
            if (stage != null && (stage.contains("1_0") || stage.contains("1_1")) && r.getParticipantAName() != null && r.getParticipantBName() != null) {
                firstRoundMatchId = r.getId();
                break;
            }
        }
        
        assertThat(firstRoundMatchId).isNotNull();

        // 8. Update Match Result
        MatchResultRequest matchResReq = MatchResultRequest.builder()
                .score(Map.of(pIds[0].toString(), 2, pIds[1].toString(), 0))
                .winnerId(pIds[0])
                .forfeit(false)
                .build();
                
        String matchResJson = objectMapper.writeValueAsString(matchResReq);
        mockMvc.perform(patch("/api/v1/matches/" + firstRoundMatchId + "/result")
                .header("Authorization", adminToken)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(matchResJson)))
                .andExpect(status().isOk());

        // 9. Fetch Standings
        MvcResult standingsResult = mockMvc.perform(get("/api/v1/events/" + eventId + "/standings")
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        String json = standingsResult.getResponse().getContentAsString();
        assertThat(json).contains("points");
    }
}

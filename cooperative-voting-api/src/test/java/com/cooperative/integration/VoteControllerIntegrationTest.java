package com.cooperative.integration;

import com.cooperative.dto.*;
import com.cooperative.repository.AgendaRepository;
import com.cooperative.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.cooperative.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class VoteControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        agendaRepository.deleteAll();
    }

    @Test
    @DisplayName("Should submit vote successfully")
    void submitVote_Success() {
        AgendaRequestDto createRequest = createAgendaRequestDto();
        ResponseEntity<AgendaResponseDto> createResponse = restTemplate.postForEntity("/agendas", createRequest, AgendaResponseDto.class);
        long agendaId = createResponse.getBody().getId();
        OpenAgendaRequestDto openBody = new OpenAgendaRequestDto(10L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OpenAgendaRequestDto> openEntity = new HttpEntity<>(openBody, headers);
        
        restTemplate.exchange("/agendas/" + agendaId + "/open", HttpMethod.PATCH, openEntity, Void.class);

        VoteRequestDto voteRequest = createVoteRequestDtoYes();
        ResponseEntity<Void> voteResponse = restTemplate.postForEntity("/votes/" + agendaId + "/votes", voteRequest, Void.class);

        assertEquals(HttpStatus.CREATED, voteResponse.getStatusCode());
    }

    @Test
    @DisplayName("Should return 400 when vote is invalid")
    void submitVote_InvalidVote_Failure() {
        Map<String, Object> invalidVoteBody = new HashMap<>();
        invalidVoteBody.put("userId", 1L);

        String invalidVoteValue = "INVALID_VOTE_TYPE";
        invalidVoteBody.put("vote", invalidVoteValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> voteEntity = new HttpEntity<>(invalidVoteBody, headers);

        ResponseEntity<String> voteResponse = restTemplate.exchange(
                "/votes/" + 1 + "/votes",
                HttpMethod.POST,
                voteEntity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, voteResponse.getStatusCode());

        String expectedErrorPart = "INVALID_VOTE_TYPE is not a valid vote type. Use YES or NO.";
        assertTrue(voteResponse.getBody().contains(expectedErrorPart));
    }

    @Test
    @DisplayName("Should get vote results successfully")
    void voteCounter_Success() {
        AgendaRequestDto createRequest = createAgendaRequestDto();

        ResponseEntity<AgendaResponseDto> createResponse =
                restTemplate.postForEntity("/agendas", createRequest, AgendaResponseDto.class);
        Long agendaId = createResponse.getBody().getId();

        Map<String, Object> openBody = new HashMap<>();
        openBody.put("durationMinutes", 5);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> openEntity = new HttpEntity<>(openBody, headers);
        restTemplate.exchange("/agendas/" + agendaId + "/open", HttpMethod.PATCH, openEntity, Void.class);

        VoteRequestDto voteRequest1 = createVoteRequestDtoYes();
        restTemplate.postForEntity("/votes/" + agendaId + "/votes", voteRequest1, Void.class);
        
        VoteRequestDto voteRequest2 = createVoteRequestDtoNo();
        restTemplate.postForEntity("/votes/" + agendaId + "/votes", voteRequest2, Void.class);

        var agenda = agendaRepository.findById(agendaId).orElseThrow();
        agenda.setStartTime(LocalDateTime.now().minusMinutes(2));
        agenda.setEndTime(LocalDateTime.now().minusMinutes(1));
        agendaRepository.saveAndFlush(agenda);

        ResponseEntity<VoteResponseDto> resultResponse =
                restTemplate.getForEntity("/votes/" + agendaId + "/result", VoteResponseDto.class);
        
        assertEquals(HttpStatus.OK, resultResponse.getStatusCode());
        assertNotNull(resultResponse.getBody());
        assertEquals(1, resultResponse.getBody().getYesVotes());
        assertEquals(1, resultResponse.getBody().getNoVotes());
        assertEquals(2, resultResponse.getBody().getTotal());
    }
}
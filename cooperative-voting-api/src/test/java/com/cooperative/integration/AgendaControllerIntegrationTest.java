package com.cooperative.integration;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionResponseDto;
import com.cooperative.dto.OpenAgendaRequestDto;
import com.cooperative.repository.AgendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.cooperative.util.TestUtils.createAgendaRequestDto;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AgendaControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AgendaRepository agendaRepository;

    @BeforeEach
    void setUp() {
        agendaRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create agenda successfully")
    void createAgenda_Success() {
        AgendaRequestDto requestDto = createAgendaRequestDto();

        ResponseEntity<AgendaResponseDto> response = restTemplate.postForEntity("/agendas", requestDto, AgendaResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(requestDto.getTitle(), response.getBody().getTitle());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @DisplayName("Should return 400 when duration is zero")
    void openAgenda_ZeroDuration_Failure() {
        AgendaRequestDto createRequest = createAgendaRequestDto();
        ResponseEntity<AgendaResponseDto> createResponse = restTemplate.postForEntity("/agendas", createRequest, AgendaResponseDto.class);
        long agendaId = createResponse.getBody().getId();

        OpenAgendaRequestDto openRequest = new OpenAgendaRequestDto(0L);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OpenAgendaRequestDto> entity = new HttpEntity<>(openRequest, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                "/agendas/" + agendaId + "/open",
                HttpMethod.PATCH,
                entity,
                Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should use default duration when body is missing")
    void openAgenda_MissingBody_Success() {
        AgendaRequestDto createRequest = createAgendaRequestDto();
        ResponseEntity<AgendaResponseDto> createResponse = restTemplate.postForEntity("/agendas", createRequest, AgendaResponseDto.class);
        long agendaId = createResponse.getBody().getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        ResponseEntity<AgendaSessionResponseDto> response = restTemplate.exchange(
                "/agendas/" + agendaId + "/open",
                HttpMethod.PATCH,
                entity,
                AgendaSessionResponseDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(agendaId, response.getBody().getAgendaId());
        assertNotNull(response.getBody().getEndTime());
    }
}
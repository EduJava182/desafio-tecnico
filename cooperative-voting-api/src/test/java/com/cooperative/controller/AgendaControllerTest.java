package com.cooperative.controller;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionResponseDto;
import com.cooperative.dto.OpenAgendaRequestDto;
import com.cooperative.service.inter.AgendaServiceI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.cooperative.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgendaControllerTest")
class AgendaControllerTest {

    @InjectMocks
    private AgendaController agendaController;

    @Mock
    private AgendaServiceI agendaService;

    @Test
    @DisplayName("Should create agenda successfully")
    void createAgenda_Success() {
        AgendaRequestDto agendaRequestDto = createAgendaRequestDto();
        AgendaResponseDto agendaResponseDto = createAgendaResponseDto();

        when(agendaService.createAgenda(agendaRequestDto)).thenReturn(agendaResponseDto);

        ResponseEntity<AgendaResponseDto> response = agendaController.createAgenda(agendaRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(agendaResponseDto.getTitle(), response.getBody().getTitle());
    }

    @Test
    @DisplayName("Should open agenda session successfully")
    void openAgenda_Success() {
        OpenAgendaRequestDto requestDto = openAgendaRequestDto();

        AgendaSessionResponseDto agendaSessionResponseDto = agendaSessionResponseDto();
        long agendaId = agendaSessionResponseDto.getAgendaId();

        when(agendaService.openAgenda(eq(agendaId), eq(requestDto)))
                .thenReturn(agendaSessionResponseDto);

        ResponseEntity<AgendaSessionResponseDto> response = agendaController.openAgenda(agendaId, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(agendaId, response.getBody().getAgendaId());
    }
}
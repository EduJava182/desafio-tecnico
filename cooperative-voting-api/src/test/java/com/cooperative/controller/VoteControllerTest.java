package com.cooperative.controller;

import com.cooperative.dto.VoteRequestDto;
import com.cooperative.dto.VoteResponseDto;
import com.cooperative.service.inter.VoteServiceI;
import com.cooperative.util.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static com.cooperative.util.TestUtils.createVoteRequestDtoYes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("VoteControllerTest")
class VoteControllerTest {

    @InjectMocks
    private VoteController voteController;

    @Mock
    private VoteServiceI voteService;

    @Test
    @DisplayName("Should submit vote successfully")
    void submitVote_Success() {
        long agendaId = 1L;
        VoteRequestDto requestDto = createVoteRequestDtoYes();

        doNothing().when(voteService).submitVote(agendaId, requestDto);

        ResponseEntity<Void> response = voteController.submitVote(agendaId, requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should get vote results successfully")
    void voteCounter_Success() {
        VoteResponseDto responseDto = TestUtils.createVoteResponseDto();
        long agendaId = responseDto.getAgendaId();

        when(voteService.voteCounter(agendaId)).thenReturn(responseDto);

        ResponseEntity<VoteResponseDto> response = voteController.voteCounter(agendaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto.getYesVotes(), response.getBody().getYesVotes());
    }
}
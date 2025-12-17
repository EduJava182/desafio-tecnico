package com.cooperative.service;

import com.cooperative.dto.VoteRequestDto;
import com.cooperative.dto.VoteResponseDto;
import com.cooperative.exception.UserAlreadyVotedException;
import com.cooperative.exception.VoteSessionException;
import com.cooperative.model.Agenda;
import com.cooperative.model.Vote;
import com.cooperative.repository.VoteRepository;
import com.cooperative.service.impl.VoteServiceImpl;
import com.cooperative.service.inter.AgendaServiceI;
import com.cooperative.service.inter.VoteResultProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.cooperative.util.TestUtils.createAgendaModel;
import static com.cooperative.util.TestUtils.createVoteRequestDtoYes;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VoteServiceImplTest")
class VoteServiceImplTest {

    @InjectMocks
    private VoteServiceImpl voteService;

    @Mock
    private AgendaServiceI agendaService;

    @Mock
    private VoteRepository voteRepository;

    @Test
    @DisplayName("Should submit vote successfully")
    void submitVote_Success() {
        long agendaId = 1L;
        VoteRequestDto requestDto = createVoteRequestDtoYes();

        Agenda agenda = createAgendaModel();

        when(agendaService.findById(agendaId)).thenReturn(agenda);
        doNothing().when(agendaService).validateAgendaInVoting(agenda);
        when(voteRepository.existsByAgendaIdAndUserId(agendaId, requestDto.getUserId())).thenReturn(false);

        assertDoesNotThrow(() -> voteService.submitVote(agendaId, requestDto));

        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    @DisplayName("Should throw exception when user already voted")
    void submitVote_UserAlreadyVoted() {
        long agendaId = 1L;
        VoteRequestDto requestDto = createVoteRequestDtoYes();

        Agenda agenda = Agenda.builder().id(agendaId).build();

        when(agendaService.findById(agendaId)).thenReturn(agenda);
        doNothing().when(agendaService).validateAgendaInVoting(agenda);
        when(voteRepository.existsByAgendaIdAndUserId(agendaId, requestDto.getUserId())).thenReturn(true);

        assertThrows(UserAlreadyVotedException.class, () -> voteService.submitVote(agendaId, requestDto));
        verify(voteRepository, never()).save(any(Vote.class));
    }

    @Test
    @DisplayName("Should count votes successfully")
    void voteCounter_Success() {
        long agendaId = 1L;
        Agenda agenda = createAgendaModel();
        VoteResultProjection projection = mock(VoteResultProjection.class);

        when(agendaService.findById(agendaId)).thenReturn(agenda);
        when(agendaService.isVotingOpen(agenda)).thenReturn(false);
        when(voteRepository.countVotesFinal(agendaId)).thenReturn(projection);
        when(projection.getYesVotes()).thenReturn(10L);
        when(projection.getNoVotes()).thenReturn(5L);
        when(projection.getTotalVotes()).thenReturn(15L);

        VoteResponseDto result = voteService.voteCounter(agendaId);

        assertNotNull(result);
        assertEquals(10L, result.getYesVotes());
        assertEquals(5L, result.getNoVotes());
        assertEquals(15L, result.getTotal());
    }

    @Test
    @DisplayName("Should throw exception when counting votes while session is open")
    void voteCounter_SessionOpen() {
        Agenda agenda = createAgendaModel();
        long agendaId = agenda.getId();

        when(agendaService.findById(agendaId)).thenReturn(agenda);
        when(agendaService.isVotingOpen(agenda)).thenReturn(true);

        assertThrows(VoteSessionException.class, () -> voteService.voteCounter(agendaId));
        verify(voteRepository, never()).countVotesFinal(anyLong());
    }

    @Test
    @DisplayName("Should throw exception when counting votes for an agenda that was never opened")
    void voteCounter_SessionNeverOpened() {
        Agenda agenda = createAgendaModel();
        agenda.setStartTime(null);
        long agendaId = agenda.getId();

        when(agendaService.findById(agendaId)).thenReturn(agenda);

        VoteSessionException exception = assertThrows(VoteSessionException.class,
                () -> voteService.voteCounter(agendaId));

        assertEquals("Voting session has not started yet.", exception.getMessage());

        verify(agendaService, never()).isVotingOpen(any());
        verify(voteRepository, never()).countVotesFinal(anyLong());
    }

    @Test
    @DisplayName("Should check if user already voted")
    void checkIfUserAlreadyVoted_True() {
        long agendaId = 1L;
        long userId = 123L;

        when(voteRepository.existsByAgendaIdAndUserId(agendaId, userId)).thenReturn(true);

        assertThrows(UserAlreadyVotedException.class, () -> voteService.checkIfUserAlreadyVoted(agendaId, userId));
    }

    @Test
    @DisplayName("Should check if user has not voted")
    void checkIfUserAlreadyVoted_False() {
        long agendaId = 1L;
        long userId = 123L;

        when(voteRepository.existsByAgendaIdAndUserId(agendaId, userId)).thenReturn(false);

        assertDoesNotThrow(() -> voteService.checkIfUserAlreadyVoted(agendaId, userId));
    }
}
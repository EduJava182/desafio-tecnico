package com.cooperative.service;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionResponseDto;
import com.cooperative.exception.AgendaAlreadyExistsException;
import com.cooperative.exception.AgendaNotFoundException;
import com.cooperative.exception.VoteSessionException;
import com.cooperative.model.Agenda;
import com.cooperative.repository.AgendaRepository;
import com.cooperative.service.impl.AgendaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.Optional;
import static com.cooperative.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceImplTest {

    @InjectMocks
    private AgendaServiceImpl agendaService;

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        agendaRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create agenda successfully")
    void createAgenda_Success() {
        AgendaRequestDto requestDto = createAgendaRequestDto();

        Agenda agenda = createAgendaModel();

        AgendaResponseDto responseDto = createAgendaResponseDto();

        when(agendaRepository.existsByTitle(requestDto.getTitle())).thenReturn(false);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(modelMapper.map(agenda, AgendaResponseDto.class)).thenReturn(responseDto);

        AgendaResponseDto result = agendaService.createAgenda(requestDto);

        assertNotNull(result);
        assertEquals(responseDto.getTitle(), result.getTitle());
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    @DisplayName("Should throw exception when creating agenda with existing title")
    void createAgenda_AlreadyExists() {
        AgendaRequestDto requestDto = createAgendaRequestDto();

        when(agendaRepository.existsByTitle(requestDto.getTitle())).thenReturn(true);

        assertThrows(AgendaAlreadyExistsException.class, () -> agendaService.createAgenda(requestDto));
        verify(agendaRepository, never()).save(any(Agenda.class));
    }

    @Test
    @DisplayName("Should open agenda session successfully")
    void openAgenda_Success() {
        long agendaId = 1L;
        Agenda agenda = createAgendaOpenNewModel();
        AgendaSessionResponseDto sessionDto = createAgendaSessionDto();

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(modelMapper.map(agenda, AgendaSessionResponseDto.class)).thenReturn(sessionDto);

        AgendaSessionResponseDto result = agendaService.openAgenda(agendaId, openAgendaRequestDto());

        assertNotNull(result);
        verify(agendaRepository).save(agenda);
    }

    @Test
    @DisplayName("Should throw exception when opening session for non-existent agenda")
    void openAgenda_AgendaNotFound() {
        long agendaId = 1L;
        when(agendaRepository.findById(agendaId)).thenReturn(Optional.empty());

        assertThrows(AgendaNotFoundException.class, () -> agendaService.openAgenda(agendaId, openAgendaRequestDto()));
    }

    @Test
    @DisplayName("Should throw exception when session is already open")
    void openAgenda_AlreadyOpen() {
        long agendaId = 1L;

        Agenda agenda = createAgendaOpenModel();

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));

        assertThrows(VoteSessionException.class, () -> agendaService.openAgenda(agendaId, openAgendaRequestDto()));
    }

    @Test
    @DisplayName("Should throw exception when session is already closed")
    void openAgenda_AlreadyClosed() {
        long agendaId = 1L;

        Agenda agenda = createAgendaClosedModel();

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));

        assertThrows(VoteSessionException.class, () -> agendaService.openAgenda(agendaId, openAgendaRequestDto()));
    }

    @Test
    @DisplayName("Should find agenda by ID successfully")
    void findById_Success() {
        Agenda agenda = createAgendaFindByIdModel();
        Long id = agenda.getId();

        when(agendaRepository.findById(id)).thenReturn(Optional.of(agenda));

        Agenda result = agendaService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("Should throw exception when finding agenda by invalid ID")
    void findById_NotFound() {
        long id = 1L;
        when(agendaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AgendaNotFoundException.class, () -> agendaService.findById(id));
    }

    @Test
    @DisplayName("Should validate agenda in voting successfully")
    void validateAgendaInVoting_Success() {
        Agenda agenda = createAgendaOpenModel();

        assertDoesNotThrow(() -> agendaService.validateAgendaInVoting(agenda));
    }

    @Test
    @DisplayName("Should throw exception when voting not started")
    void validateAgendaInVoting_NotStarted() {
        Agenda agenda = createAgendaNotStartedToVoteModel();

        assertThrows(VoteSessionException.class, () -> agendaService.validateAgendaInVoting(agenda));
    }

    @Test
    @DisplayName("Should throw exception when voting is closed")
    void validateAgendaInVoting_Closed() {
        Agenda agenda = createAgendaClosedModel();

        assertThrows(VoteSessionException.class, () -> agendaService.validateAgendaInVoting(agenda));
    }

    @Test
    @DisplayName("Should check if voting is open")
    void isVotingOpen_Check() {
        Agenda openAgenda = createAgendaOpenModel();
        assertTrue(agendaService.isVotingOpen(openAgenda));

        Agenda closedAgenda = createAgendaClosedModel();
        assertFalse(agendaService.isVotingOpen(closedAgenda));

        Agenda notStartedAgenda = createAgendaNotStartedToVoteModel();
        assertFalse(agendaService.isVotingOpen(notStartedAgenda));
    }
}
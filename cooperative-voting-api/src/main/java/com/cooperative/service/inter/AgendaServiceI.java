package com.cooperative.service.inter;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionDto;
import com.cooperative.model.Agenda;

import java.util.List;

public interface AgendaServiceI {

    AgendaResponseDto createAgenda(AgendaRequestDto agendaRequestDto);

    List<AgendaResponseDto> getAllAgendas();

    AgendaSessionDto openAgenda(long agendaId, long duration);

    void verifyByTitle(String title);

    Agenda findById(long agendaId);

    void validateAgendaInVoting(Agenda agenda);

    boolean isVotingOpen(Agenda agenda);
}

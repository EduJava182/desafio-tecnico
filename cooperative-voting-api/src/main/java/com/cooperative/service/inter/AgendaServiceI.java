package com.cooperative.service.inter;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionResponseDto;
import com.cooperative.dto.OpenAgendaRequestDto;
import com.cooperative.model.Agenda;

public interface AgendaServiceI {

    AgendaResponseDto createAgenda(AgendaRequestDto agendaRequestDto);

    AgendaSessionResponseDto openAgenda(long agendaId, OpenAgendaRequestDto openAgendaRequestDto);

    void verifyByTitle(String title);

    Agenda findById(long agendaId);

    void validateAgendaInVoting(Agenda agenda);

    boolean isVotingOpen(Agenda agenda);

    boolean isVotingClosed(Agenda agenda);
}

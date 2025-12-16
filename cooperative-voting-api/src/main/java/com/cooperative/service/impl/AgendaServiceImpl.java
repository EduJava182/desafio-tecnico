package com.cooperative.service.impl;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionDto;
import com.cooperative.exception.AgendaAlreadyExistsException;
import com.cooperative.exception.AgendaNotFoundException;
import com.cooperative.exception.VoteSessionException;
import com.cooperative.model.Agenda;
import com.cooperative.repository.AgendaRepository;
import com.cooperative.service.inter.AgendaServiceI;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaServiceI {

    private final ModelMapper modelMapper;
    private final AgendaRepository agendaRepository;

    @Override
    public AgendaResponseDto createAgenda(AgendaRequestDto agendaRequestDto) {
        String title = agendaRequestDto.title();

        this.verifyByTitle(title);

        Agenda agenda = Agenda.builder()
                .title(title)
                .description(agendaRequestDto.description())
                .build();

        return modelMapper.map(agendaRepository.save(agenda), AgendaResponseDto.class);
    }

    @Override
    public List<AgendaResponseDto> getAllAgendas() {
        return agendaRepository.findAll().stream()
                .map(agenda -> modelMapper.map(agenda, AgendaResponseDto.class))
                .toList();
    }

    @Override
    public AgendaSessionDto openAgenda(long agendaId, long durationMinutes) {
        Agenda agendaSaved = this.findById(agendaId);

        if (this.isVotingOpen(agendaSaved)) {
            throw new VoteSessionException("The voting session is already open.");
        }

        LocalDateTime now = LocalDateTime.now();
        agendaSaved.setStartTime(now);
        agendaSaved.setEndTime(now.plusMinutes(durationMinutes));

        return modelMapper.map(agendaRepository.save(agendaSaved), AgendaSessionDto.class);
    }

    @Override
    public void verifyByTitle(String title) {
        if (agendaRepository.verifyByTitle(title)) {
            throw new AgendaAlreadyExistsException("An agenda with the title already exists.: " + title);
        }
    }

    @Override
    public Agenda findById(long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new AgendaNotFoundException("Agenda not found for id: " + id));
    }

    @Override
    public void validateAgendaInVoting(Agenda agenda) {
        LocalDateTime now = LocalDateTime.now();

        if (agenda.getStartTime() == null || agenda.getEndTime() == null || now.isBefore(agenda.getStartTime())) {
            throw new VoteSessionException("Voting session has not started yet.");
        }

        if (now.isAfter(agenda.getEndTime())) {
            throw new VoteSessionException("Voting session is already closed.");
        }
    }

    @Override
    public boolean isVotingOpen(Agenda agenda) {
        LocalDateTime now = LocalDateTime.now();
        return agenda.getStartTime() != null && agenda.getEndTime() != null
                && now.isAfter(agenda.getStartTime()) && now.isBefore(agenda.getEndTime());
    }
}

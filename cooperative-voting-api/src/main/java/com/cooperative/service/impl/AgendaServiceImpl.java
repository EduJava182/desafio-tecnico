package com.cooperative.service.impl;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionResponseDto;
import com.cooperative.dto.OpenAgendaRequestDto;
import com.cooperative.exception.AgendaAlreadyExistsException;
import com.cooperative.exception.AgendaNotFoundException;
import com.cooperative.exception.VoteSessionException;
import com.cooperative.model.Agenda;
import com.cooperative.repository.AgendaRepository;
import com.cooperative.service.inter.AgendaServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaServiceI {

    private final ModelMapper modelMapper;
    private final AgendaRepository agendaRepository;

    @Override
    public AgendaResponseDto createAgenda(AgendaRequestDto agendaRequestDto) {
        String title = agendaRequestDto.getTitle().trim();
        log.info("Creating agenda. Title: {}", title);

        this.verifyByTitle(title);

        Agenda agenda = Agenda.builder()
                .title(title)
                .description(agendaRequestDto.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        Agenda savedAgenda = agendaRepository.save(agenda);

        log.info("Agenda created with ID: {}", savedAgenda.getId());
        return modelMapper.map(savedAgenda, AgendaResponseDto.class);
    }

    @Override
    public AgendaSessionResponseDto openAgenda(long agendaId, OpenAgendaRequestDto openAgendaRequestDto) {
        Agenda agendaSaved = this.findById(agendaId);

        if (!hasNotStarted(agendaSaved)) {
            log.warn("This agenda has already been opened. Agenda ID: {}", agendaId);
            throw new VoteSessionException("This agenda has already been opened before.");
        }

        long durationMinutes = Optional.ofNullable(openAgendaRequestDto)
                .map(OpenAgendaRequestDto::getDurationMinutes)
                .orElse(1L);

        log.info("Opening session for agenda ID: {} with duration: {} minutes", agendaId, durationMinutes);

        LocalDateTime now = LocalDateTime.now();
        agendaSaved.setStartTime(now);
        agendaSaved.setEndTime(now.plusMinutes(durationMinutes));

        Agenda updatedAgenda = agendaRepository.save(agendaSaved);
        log.info("Session opened for agenda ID: {}. Ends at: {}", agendaId, updatedAgenda.getEndTime());

        return modelMapper.map(updatedAgenda, AgendaSessionResponseDto.class);
    }

    @Override
    public void verifyByTitle(String title) {
        if (agendaRepository.existsByTitle(title)) {
            log.error("Agenda title already exists: {}", title);
            throw new AgendaAlreadyExistsException("An agenda with the title already exists. " + title);
        }
    }

    @Override
    public Agenda findById(long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Agenda not found. ID: {}", id);
                    return new AgendaNotFoundException(
                            "Agenda not found. ID: " + id
                    );
                });
    }

    @Override
    public void validateAgendaInVoting(Agenda agenda) {
        if (hasNotStarted(agenda)) {
            log.warn("Voting session for Agenda ID {} has not started yet.", agenda.getId());
            throw new VoteSessionException("Voting session has not started yet.");
        }
        if (isVotingClosed(agenda)) {
            log.warn("Voting session for Agenda ID {} is already closed.", agenda.getId());
            throw new VoteSessionException("Voting session is already closed.");
        }
    }

    @Override
    public boolean isVotingOpen(Agenda agenda) {
        if (agenda.getStartTime() == null) return false;

        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(agenda.getEndTime());
    }

    @Override
    public boolean isVotingClosed(Agenda agenda) {
        if (agenda.getStartTime() == null) return false;

        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(agenda.getEndTime()) || now.isEqual(agenda.getEndTime());
    }

    private boolean hasNotStarted(Agenda agenda) {
        return agenda.getStartTime() == null;
    }
}

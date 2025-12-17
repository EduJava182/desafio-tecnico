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

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaServiceI {

    private final ModelMapper modelMapper;
    private final AgendaRepository agendaRepository;

    @Override
    public AgendaResponseDto createAgenda(AgendaRequestDto agendaRequestDto) {
        String title = agendaRequestDto.getTitle();
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
        long durationMinutes = (openAgendaRequestDto != null && openAgendaRequestDto.getDurationMinutes() != null)
                ? openAgendaRequestDto.getDurationMinutes() : 1L;

        log.info("Opening session for agenda ID: {} with duration: {} minutes", agendaId, durationMinutes);

        Agenda agendaSaved = this.findById(agendaId);
        LocalDateTime startTime = agendaSaved.getStartTime();
        LocalDateTime endTime = agendaSaved.getEndTime();
        LocalDateTime now = LocalDateTime.now();

        if (startTime != null && endTime != null) {
            if (this.isVotingOpen(agendaSaved)) {
                log.warn("Voting session already open. Agenda ID: {}", agendaId);
                throw new VoteSessionException("Voting session is already open.");
            }
            if (agendaSaved.getEndTime().isBefore(now)) {
                log.warn("Voting session already closed. Agenda ID: {}", agendaId);
                throw new VoteSessionException(
                        "Voting session is already closed and cannot be reopened."
                );
            }
        }

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
            throw new AgendaAlreadyExistsException("An agenda with the title already exists.: " + title);
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
        long agendaId = agenda.getId();
        LocalDateTime startTime = agenda.getStartTime();
        LocalDateTime endTime = agenda.getEndTime();
        LocalDateTime now = LocalDateTime.now();

        if (startTime == null || endTime == null || now.isBefore(startTime)) {
            log.warn("Voting session for Agenda ID {} has not started yet.", agendaId);
            throw new VoteSessionException("Voting session has not started yet.");
        }

        if (now.isAfter(endTime)) {
            log.warn("Voting session for Agenda ID {} is already closed.", agendaId);
            throw new VoteSessionException("Voting session is already closed.");
        }
    }

    @Override
    public boolean isVotingOpen(Agenda agenda) {
        LocalDateTime startTime = agenda.getStartTime();
        LocalDateTime endTime = agenda.getEndTime();
        LocalDateTime now = LocalDateTime.now();

        return startTime != null
                && endTime != null
                && now.isAfter(startTime)
                && now.isBefore(endTime);
    }
}

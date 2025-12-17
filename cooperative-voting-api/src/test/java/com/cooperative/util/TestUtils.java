package com.cooperative.util;

import com.cooperative.dto.*;
import com.cooperative.model.Agenda;

import java.time.LocalDateTime;

import static com.cooperative.enumeratiom.VoteTypeEnum.*;

public class TestUtils {

    public static AgendaRequestDto createAgendaRequestDto() {
        return AgendaRequestDto.builder()
                .title("New Agenda Request")
                .description("Agenda test description")
                .build();
    }

    public static AgendaResponseDto createAgendaResponseDto() {
        return AgendaResponseDto.builder()
                .id(1L)
                .title("New Agenda Response")
                .description("Agenda test description")
                .build();
    }

    public static AgendaSessionResponseDto agendaSessionResponseDto() {
        return AgendaSessionResponseDto.builder()
                .agendaId(10L)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    public static OpenAgendaRequestDto openAgendaRequestDto() {
        return OpenAgendaRequestDto.builder()
                .durationMinutes(10L)
                .build();
    }

    public static VoteRequestDto createVoteRequestDtoYes() {
        return VoteRequestDto.builder()
                .userId(1L)
                .vote(YES)
                .build();
    }

    public static VoteRequestDto createVoteRequestDtoNo() {
        return VoteRequestDto.builder()
                .userId(2L)
                .vote(NO)
                .build();
    }

    public static VoteResponseDto createVoteResponseDto() {
        return VoteResponseDto.builder()
                .agendaId(123L)
                .yesVotes(10L)
                .noVotes(5L)
                .total(15L)
                .build();
    }

    public static Agenda createAgendaModel() {
        return Agenda.builder()
                .id(1L)
                .title("New Agenda")
                .description("Description")
                .createdAt(LocalDateTime.now())
                .startTime(LocalDateTime.now().minusMinutes(2))
                .endTime(LocalDateTime.now().plusMinutes(1))
                .build();
    }

    public static Agenda createAgendaOpenModel() {
        return Agenda.builder()
                .id(1L)
                .startTime(LocalDateTime.now().minusMinutes(1))
                .endTime(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    public static Agenda createAgendaOpenNewModel() {
        return Agenda.builder()
                .id(1L)
                .startTime(null)
                .endTime(null)
                .build();
    }

    public static Agenda createAgendaClosedModel() {
        return Agenda.builder()
                .id(1L)
                .startTime(LocalDateTime.now().minusMinutes(20))
                .endTime(LocalDateTime.now().minusMinutes(10))
                .build();
    }

    public static AgendaSessionResponseDto createAgendaSessionDto() {
        return AgendaSessionResponseDto.builder()
                .agendaId(123L)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    public static Agenda createAgendaFindByIdModel() {
        return Agenda.builder()
                .id(1L)
                .build();
    }

    public static Agenda createAgendaNotStartedToVoteModel() {
        return Agenda.builder()
                .id(1L)
                .startTime(null)
                .endTime(null)
                .build();
    }
}

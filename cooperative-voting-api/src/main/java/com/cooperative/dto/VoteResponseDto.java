package com.cooperative.dto;

public record VoteResponseDto(

    long agendaId,
    long yesVotes,
    long noVotes,
    long total
) {}

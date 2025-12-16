package com.cooperative.service.inter;

import com.cooperative.dto.VoteRequestDto;
import com.cooperative.dto.VoteResponseDto;

public interface VoteServiceI {

    void submitVote(Long agendaId, VoteRequestDto voteRequestDto);

    VoteResponseDto voteCounter(long agendaId);

    void checkIfUserAlreadyVoted(long agendaId, long userId);
}

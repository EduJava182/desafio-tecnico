package com.cooperative.service.impl;

import com.cooperative.dto.VoteRequestDto;
import com.cooperative.dto.VoteResponseDto;
import com.cooperative.exception.UserAlreadyVotedException;
import com.cooperative.model.Agenda;
import com.cooperative.model.Vote;
import com.cooperative.repository.VoteRepository;
import com.cooperative.service.inter.AgendaServiceI;
import com.cooperative.service.inter.VoteResultProjection;
import com.cooperative.service.inter.VoteServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteServiceI {

    private final AgendaServiceI agendaServiceI;
    private final VoteRepository voteRepository;

    @Override
    public void submitVote(Long agendaId, VoteRequestDto voteRequestDto) {
        Agenda agenda = agendaServiceI.findById(agendaId);
        
        agendaServiceI.validateAgendaInVoting(agenda);

        this.checkIfUserAlreadyVoted(agendaId, voteRequestDto.userId());

        Vote vote = Vote.builder()
                .agenda(agenda)
                .userId(voteRequestDto.userId())
                .voteType(voteRequestDto.vote())
                .build();

        voteRepository.save(vote);
    }

    @Override
    public VoteResponseDto voteCounter(long agendaId) {
        Agenda agenda = agendaServiceI.findById(agendaId);

        VoteResultProjection finalCount = voteRepository.countVotesFinal(agendaId);

        return new VoteResponseDto(
                agenda.getId(),
                finalCount.getYesVotes(),
                finalCount.getNoVotes(),
                finalCount.getTotalVotes()
        );
    }

    @Override
    public void checkIfUserAlreadyVoted(long agendaId, long userId) {
        if (voteRepository.checkIfUserAlreadyVoted(agendaId, userId)) {
            throw new UserAlreadyVotedException("The member has already voted on this agenda.");
        }
    }
}

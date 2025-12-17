package com.cooperative.service.impl;

import com.cooperative.dto.VoteRequestDto;
import com.cooperative.dto.VoteResponseDto;
import com.cooperative.enumeratiom.VoteTypeEnum;
import com.cooperative.exception.UserAlreadyVotedException;
import com.cooperative.exception.VoteSessionException;
import com.cooperative.model.Agenda;
import com.cooperative.model.Vote;
import com.cooperative.repository.VoteRepository;
import com.cooperative.service.inter.AgendaServiceI;
import com.cooperative.service.inter.VoteResultProjection;
import com.cooperative.service.inter.VoteServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteServiceI {

    private final AgendaServiceI agendaServiceI;
    private final VoteRepository voteRepository;

    @Override
    public void submitVote(Long agendaId, VoteRequestDto voteRequestDto) {
        long userId = voteRequestDto.getUserId();
        VoteTypeEnum voteTypeEnum = voteRequestDto.getVote();

        log.info("Submit vote request. Agenda ID: {}, User ID: {}, Vote: {}",
                agendaId, userId, voteTypeEnum);

        Agenda agenda = agendaServiceI.findById(agendaId);

        agendaServiceI.validateAgendaInVoting(agenda);

        this.checkIfUserAlreadyVoted(agendaId, userId);

        Vote vote = Vote.builder()
                .agenda(agenda)
                .userId(userId)
                .voteType(voteTypeEnum)
                .build();

        voteRepository.save(vote);

        log.info("Vote saved successfully. Agenda ID: {}, User ID: {}, Vote: {}",
                agendaId, userId, voteTypeEnum);
    }

    @Override
    public VoteResponseDto voteCounter(long agendaId) {
        Agenda agenda = agendaServiceI.findById(agendaId);

        if (!agendaServiceI.isVotingClosed(agenda)) {
            log.warn("Cannot show results. Session for Agenda ID {} is not closed.", agendaId);
            throw new VoteSessionException("Results are available only after the session is closed.");
        }

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
        if (voteRepository.existsByAgendaIdAndUserId(agendaId, userId)) {
            log.warn("Vote rejected. User {} already voted on agenda {}", userId, agendaId);
            throw new UserAlreadyVotedException("The member has already voted on this agenda.");
        }
    }
}

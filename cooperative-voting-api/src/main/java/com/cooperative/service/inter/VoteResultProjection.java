package com.cooperative.service.inter;

public interface VoteResultProjection {

    Long getAgendaId();
    Long getYesVotes();
    Long getNoVotes();
    Long getTotalVotes();
}

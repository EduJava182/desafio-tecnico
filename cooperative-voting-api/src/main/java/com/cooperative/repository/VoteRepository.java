package com.cooperative.repository;

import com.cooperative.model.Vote;
import com.cooperative.service.inter.VoteResultProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean checkIfUserAlreadyVoted(Long agendaId, Long userId);

    @Query("""
        SELECT 
            v.agenda.id AS agendaId,
            SUM(CASE WHEN v.vote = 'SIM' THEN 1 ELSE 0 END) AS yesVotes,
            SUM(CASE WHEN v.vote = 'NAO' THEN 1 ELSE 0 END) AS noVotes,
            COUNT(v) AS totalVotes
        FROM Vote v
        WHERE v.agenda.id = :agendaId
    """)
    VoteResultProjection countVotesFinal(@Param("agendaId") long agendaId);
}

package com.cooperative.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponseDto {

    private long agendaId;
    private long yesVotes;
    private long noVotes;
    private long total;
}


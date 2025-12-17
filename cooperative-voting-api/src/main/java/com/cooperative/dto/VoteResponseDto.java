package com.cooperative.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponseDto {

    private long agendaId;
    private long yesVotes;
    private long noVotes;
    private long total;
}


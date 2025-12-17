package com.cooperative.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponseDto {

    @Schema(defaultValue = "0")
    private long agendaId;

    @Schema(defaultValue = "0")
    private long yesVotes;

    @Schema(defaultValue = "0")
    private long noVotes;

    @Schema(defaultValue = "0")
    private long total;
}


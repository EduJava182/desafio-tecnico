package com.cooperative.dto;

import com.cooperative.enumeratiom.VoteTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestDto {

    @NotNull(message = "User Id is required")
    private Long userId;

    @NotNull(message = "Vote is required")
    private VoteTypeEnum vote;
}

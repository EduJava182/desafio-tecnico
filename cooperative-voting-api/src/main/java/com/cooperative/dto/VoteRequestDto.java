package com.cooperative.dto;

import com.cooperative.VoteTypeEnum;

import javax.validation.constraints.NotNull;

public record VoteRequestDto(

        @NotNull(message = "UserId is required")
        Long userId,

        @NotNull(message = "Vote is required")
        VoteTypeEnum vote
) {}

package com.cooperative.dto;


import com.cooperative.enumeratiom.VoteTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestDto {

    @NotNull(message = "UserId is required")
    private Long userId;

    @NotNull(message = "Vote is required")
    private VoteTypeEnum vote;
}

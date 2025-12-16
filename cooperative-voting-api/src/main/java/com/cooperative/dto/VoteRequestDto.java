package com.cooperative.dto;


import com.cooperative.enumeratiom.VoteTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @NotNull(message = "Vote type must be YES or NO")
    @Schema(
            description = "Vote value",
            allowableValues = {"YES", "NO"},
            example = "YES",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private VoteTypeEnum vote;
}

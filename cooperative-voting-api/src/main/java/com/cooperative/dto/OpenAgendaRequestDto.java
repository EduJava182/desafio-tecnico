package com.cooperative.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAgendaRequestDto {

    @Schema(defaultValue = "1")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Long durationMinutes;
}

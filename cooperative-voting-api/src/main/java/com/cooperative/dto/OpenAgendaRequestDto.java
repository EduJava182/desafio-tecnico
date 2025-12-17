package com.cooperative.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAgendaRequestDto {

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Long durationMinutes;
}

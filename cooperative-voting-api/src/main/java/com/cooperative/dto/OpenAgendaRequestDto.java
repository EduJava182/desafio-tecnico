package com.cooperative.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class OpenAgendaRequestDto {

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private long durationMinutes;
}

package com.cooperative.dto;

import javax.validation.constraints.NotBlank;

public record AgendaRequestDto(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description
) {}

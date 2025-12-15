package com.cooperative.dto;

import java.time.LocalDateTime;

public record AgendaResponseDto(

    long id,
    String title,
    String description,
    LocalDateTime createdAt
) {}

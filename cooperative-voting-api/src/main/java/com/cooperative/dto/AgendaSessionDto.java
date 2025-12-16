package com.cooperative.dto;

import java.time.LocalDateTime;

public record AgendaSessionDto(

        long sessionId,
        long agendaId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}

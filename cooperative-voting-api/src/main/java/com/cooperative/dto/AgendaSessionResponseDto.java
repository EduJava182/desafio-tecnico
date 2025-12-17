package com.cooperative.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaSessionResponseDto {

    private long agendaId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

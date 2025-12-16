package com.cooperative.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendaSessionDto {

    private long agendaId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

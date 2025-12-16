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
public class AgendaResponseDto {

    private long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}




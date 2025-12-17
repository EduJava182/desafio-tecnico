package com.cooperative.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaResponseDto {

    private long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}




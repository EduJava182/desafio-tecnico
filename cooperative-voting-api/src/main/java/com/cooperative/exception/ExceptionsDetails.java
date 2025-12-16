package com.cooperative.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionsDetails {

    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
}

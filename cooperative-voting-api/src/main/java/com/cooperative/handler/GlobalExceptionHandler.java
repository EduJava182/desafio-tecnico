package com.cooperative.handler;

import com.cooperative.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionsDetails> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fieldMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("Validation error: {}", fieldMessage);

        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(fieldMessage)
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AgendaNotFoundException.class)
    public ResponseEntity<ExceptionsDetails> handleAgendaNotFoundException(AgendaNotFoundException ex) {
        log.warn("Agenda not found: {}", ex.getMessage());

        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AgendaAlreadyExistsException.class)
    public ResponseEntity<ExceptionsDetails> handleAgendaAlreadyExistsException(AgendaAlreadyExistsException ex) {
        log.warn("Agenda already exists: {}", ex.getMessage());

        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VoteSessionException.class)
    public ResponseEntity<ExceptionsDetails> handleVotingSessionException(VoteSessionException ex) {
        log.warn("Voting session exception: {}", ex.getMessage());

        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyVotedException.class)
    public ResponseEntity<ExceptionsDetails> handleUserAlreadyVotedException(UserAlreadyVotedException ex) {
        log.warn("User already voted: {}", ex.getMessage());

        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionsDetails> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Invalid request body";

        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof IllegalArgumentException iae) {
            message = iae.getMessage();
        }

        log.warn("JSON parse error: {}", message);

        return ResponseEntity.badRequest().body(
                ExceptionsDetails.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionsDetails> handleGeneralException(Exception ex) {
        log.error("Unexpected internal error", ex);

        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


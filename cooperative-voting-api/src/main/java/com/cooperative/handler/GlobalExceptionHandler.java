package com.cooperative.handler;

import com.cooperative.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionsDetails> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fieldMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(fieldMessage)
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AgendaNotFoundException.class)
    public ResponseEntity<ExceptionsDetails> handleAgendaNotFoundException(AgendaNotFoundException ex) {
        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AgendaAlreadyExistsException.class)
    public ResponseEntity<ExceptionsDetails> handleAgendaAlreadyExistsException(AgendaAlreadyExistsException ex) {
        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VoteSessionException.class)
    public ResponseEntity<ExceptionsDetails> handleVotingSessionException(VoteSessionException ex) {
        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyVotedException.class)
    public ResponseEntity<ExceptionsDetails> handleUserAlreadyVotedException(UserAlreadyVotedException ex) {
        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionsDetails> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(ExceptionsDetails.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


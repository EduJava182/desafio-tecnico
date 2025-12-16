package com.cooperative.exception;

public class AgendaAlreadyExistsException extends RuntimeException {

    public AgendaAlreadyExistsException(String message) {
        super(message);
    }
}

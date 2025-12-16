package com.cooperative.exception;

public class AgendaNotFoundException extends RuntimeException {

    public AgendaNotFoundException(String message) {
        super(message);
    }
}

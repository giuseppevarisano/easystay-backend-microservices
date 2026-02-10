package com.easystay.auth.exception;

public class BadCredentialsException extends RuntimeException {
    
    public BadCredentialsException(String message) {
        super(message);
    }
    
    public BadCredentialsException() {
        super("Credenziali non valide");
    }
}

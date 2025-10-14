package com.example.livraison.exception;

public class CommandeNotFoundException extends RuntimeException {
    
    public CommandeNotFoundException(String message) {
        super(message);
    }
    
    public CommandeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

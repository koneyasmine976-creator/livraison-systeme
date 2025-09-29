package com.example.livraison.exception;

public class ProduitNotFoundException extends RuntimeException {
    
    public ProduitNotFoundException(String message) {
        super(message);
    }
    
    public ProduitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

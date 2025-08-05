package com.br.obitus_manager.application.exception;

public class OTPAlreadySentException extends RuntimeException {
    public OTPAlreadySentException(String message) {
        super(message);
    }
}

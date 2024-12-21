package me.itzisonn_.meazy.lexer;

public class UnknownTokenException extends RuntimeException {
    public UnknownTokenException(String message) {
        super(message);
    }
}
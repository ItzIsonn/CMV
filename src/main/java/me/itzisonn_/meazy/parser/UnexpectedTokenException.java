package me.itzisonn_.meazy.parser;

public class UnexpectedTokenException extends RuntimeException {
    public UnexpectedTokenException(String message) {
        super(message);
    }

    public UnexpectedTokenException(String message, int line) {
        super("At line " + line + ": " + message);
    }
}

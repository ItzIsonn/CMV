package me.itzisonn_.meazy.parser;

public class InvalidStatementException extends RuntimeException {
    public InvalidStatementException(String message) {
        super(message);
    }

    public InvalidStatementException(String message, int line) {
        super("At line " + line + ": " + message);
    }
}

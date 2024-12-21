package me.itzisonn_.meazy.runtime.interpreter;

public class InvalidSyntaxException extends RuntimeException {
    public InvalidSyntaxException(String message) {
        super(message);
    }
}

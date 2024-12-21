package me.itzisonn_.meazy.runtime.interpreter;

public class InvalidIdentifierException extends RuntimeException {
    public InvalidIdentifierException(String message) {
        super(message);
    }
}

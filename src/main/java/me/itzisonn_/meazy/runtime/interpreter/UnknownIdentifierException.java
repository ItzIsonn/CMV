package me.itzisonn_.meazy.runtime.interpreter;

public class UnknownIdentifierException extends RuntimeException {
    public UnknownIdentifierException(String message) {
        super(message);
    }
}

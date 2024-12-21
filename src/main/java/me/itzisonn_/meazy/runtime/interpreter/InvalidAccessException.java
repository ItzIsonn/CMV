package me.itzisonn_.meazy.runtime.interpreter;

public class InvalidAccessException extends RuntimeException {
    public InvalidAccessException(String message) {
        super(message);
    }
}

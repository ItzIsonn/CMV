package me.itzisonn_.meazy.runtime.interpreter;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}

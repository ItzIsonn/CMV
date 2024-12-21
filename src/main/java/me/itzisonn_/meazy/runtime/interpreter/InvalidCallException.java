package me.itzisonn_.meazy.runtime.interpreter;

public class InvalidCallException extends RuntimeException {
    public InvalidCallException(String message) {
        super(message);
    }
}

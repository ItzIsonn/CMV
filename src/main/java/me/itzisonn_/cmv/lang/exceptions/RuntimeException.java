package me.itzisonn_.cmv.lang.exceptions;

public class RuntimeException extends java.lang.RuntimeException {
    public RuntimeException(int line, String message) {
        super("at " + line + ": " + message);
    }
}
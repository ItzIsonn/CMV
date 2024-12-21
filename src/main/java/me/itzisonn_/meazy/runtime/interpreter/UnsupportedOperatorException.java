package me.itzisonn_.meazy.runtime.interpreter;

public class UnsupportedOperatorException extends RuntimeException {
    public UnsupportedOperatorException(String operator) {
        super("This interpreter doesn't support operator '" + operator + "'");
    }
}

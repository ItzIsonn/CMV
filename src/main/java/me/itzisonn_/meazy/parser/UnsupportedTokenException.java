package me.itzisonn_.meazy.parser;

public class UnsupportedTokenException extends RuntimeException {
    public UnsupportedTokenException(String id) {
        super("This parser doesn't support token type '" + id + "'");
    }

    public UnsupportedTokenException(String id, int line) {
        super("At line " + line + ": This parser doesn't support token type '" + id + "'");
    }
}

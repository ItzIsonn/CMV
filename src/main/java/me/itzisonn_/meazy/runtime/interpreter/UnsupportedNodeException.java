package me.itzisonn_.meazy.runtime.interpreter;

public class UnsupportedNodeException extends RuntimeException {
    public UnsupportedNodeException(String id) {
        super("This interpreter doesn't support " + id);
    }
}

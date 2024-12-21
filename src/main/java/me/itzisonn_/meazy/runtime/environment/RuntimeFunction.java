package me.itzisonn_.meazy.runtime.environment;

import lombok.Getter;
import me.itzisonn_.meazy.runtime.values.RuntimeValue;

@Getter
public class RuntimeFunction {
    private final String id;
    private final RuntimeValue<?> functionValue;

    public RuntimeFunction(String id, RuntimeValue<?> functionValue) {
        this.id = id;
        this.functionValue = functionValue;
    }
}
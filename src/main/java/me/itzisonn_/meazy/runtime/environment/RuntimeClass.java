package me.itzisonn_.meazy.runtime.environment;

import lombok.Getter;
import me.itzisonn_.meazy.runtime.values.RuntimeValue;

@Getter
public class RuntimeClass {
    private final String id;
    private final RuntimeValue<?> runtimeValue;

    public RuntimeClass(String id, RuntimeValue<?> runtimeValue) {
        this.id = id;
        this.runtimeValue = runtimeValue;
    }
}
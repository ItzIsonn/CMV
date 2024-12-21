package me.itzisonn_.meazy.runtime.values;

import lombok.Getter;
import me.itzisonn_.meazy.runtime.environment.interfaces.declaration.VariableDeclarationEnvironment;

@Getter
public class VariableValue extends RuntimeValue<RuntimeValue<?>> {
    private final VariableDeclarationEnvironment parentEnvironment;
    private final String id;

    public VariableValue(RuntimeValue<?> value, VariableDeclarationEnvironment parentEnvironment, String id) {
        super(value);
        this.parentEnvironment = parentEnvironment;
        this.id = id;
    }
}
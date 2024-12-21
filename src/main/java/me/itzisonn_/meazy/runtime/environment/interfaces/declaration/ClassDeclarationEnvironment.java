package me.itzisonn_.meazy.runtime.environment.interfaces.declaration;

import me.itzisonn_.meazy.runtime.environment.RuntimeClass;
import me.itzisonn_.meazy.runtime.environment.interfaces.Environment;
import me.itzisonn_.meazy.runtime.values.RuntimeValue;

public interface ClassDeclarationEnvironment extends Environment {
    void declareClass(String id, RuntimeValue<?> value);

    RuntimeClass getClass(String id);
}
package me.itzisonn_.meazy.runtime.environment.interfaces.declaration;

import me.itzisonn_.meazy.runtime.environment.interfaces.Environment;
import me.itzisonn_.meazy.runtime.values.RuntimeValue;

import java.util.ArrayList;

public interface ConstructorDeclarationEnvironment extends Environment {
    void declareConstructor(RuntimeValue<?> value);
    RuntimeValue<?> getConstructor(ArrayList<RuntimeValue<?>> args);
    ArrayList<RuntimeValue<?>> getConstructors();
    boolean hasConstructor();
}
package me.itzisonn_.meazy.runtime.environment.interfaces;

import me.itzisonn_.meazy.runtime.environment.interfaces.declaration.FunctionDeclarationEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.declaration.VariableDeclarationEnvironment;
import me.itzisonn_.meazy.runtime.values.RuntimeValue;

import java.util.ArrayList;
import java.util.function.Predicate;

public interface Environment {
    Environment getParent();
    boolean hasParent(Environment environment);
    boolean hasParent(Predicate<Environment> predicate);

    VariableDeclarationEnvironment getVariableEnvironment(String id);
    FunctionDeclarationEnvironment getFunctionEnvironment(String id, ArrayList<RuntimeValue<?>> args);

    boolean isShared();
}
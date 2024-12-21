package me.itzisonn_.meazy.runtime.environment.basic;

import me.itzisonn_.meazy.runtime.environment.interfaces.Environment;
import me.itzisonn_.meazy.runtime.environment.interfaces.FunctionEnvironment;

public class BasicFunctionEnvironment extends BasicVariableDeclarationEnvironment implements FunctionEnvironment {
    public BasicFunctionEnvironment(Environment parent, boolean isShared) {
        super(parent, isShared);
    }

    public BasicFunctionEnvironment(Environment parent) {
        super(parent, false);
    }
}
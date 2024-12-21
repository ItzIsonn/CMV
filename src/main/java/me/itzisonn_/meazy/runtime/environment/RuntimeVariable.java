package me.itzisonn_.meazy.runtime.environment;

import lombok.Getter;
import me.itzisonn_.meazy.parser.ast.DataType;
import me.itzisonn_.meazy.runtime.interpreter.InvalidSyntaxException;
import me.itzisonn_.meazy.runtime.values.NullValue;
import me.itzisonn_.meazy.runtime.values.RuntimeValue;
import me.itzisonn_.meazy.runtime.values.VariableValue;

import java.util.Set;

@Getter
public class RuntimeVariable {
    private final String id;
    private final DataType dataType;
    private RuntimeValue<?> value;
    private final boolean isConstant;
    private final Set<String> accessModifiers;
    private final boolean isArgument;

    public RuntimeVariable(String id, DataType dataType, RuntimeValue<?> value, boolean isConstant, Set<String> accessModifiers, boolean isArgument) {
        this.id = id;
        this.dataType = dataType;
        setValue(value);
        this.isConstant = isConstant;
        this.accessModifiers = accessModifiers;
        this.isArgument = isArgument;
    }

    public void setValue(RuntimeValue<?> value) {
        if (isConstant) throw new InvalidSyntaxException("Can't reassign value of constant variable " + id);

        Object realValue = value;
        while (realValue instanceof VariableValue runtimeValue) {
            realValue = runtimeValue.getValue();
        }
        if (!dataType.isMatches(realValue) && !(realValue instanceof NullValue))
            throw new InvalidSyntaxException("Variable with id " + id + " requires data type " + dataType.getName());

        this.value = value;
    }
}
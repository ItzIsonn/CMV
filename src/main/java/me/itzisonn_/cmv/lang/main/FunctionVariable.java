package me.itzisonn_.cmv.lang.main;

import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.types.Type;
import me.itzisonn_.cmv.lang.types.VoidValue;

public class FunctionVariable extends Variable {
    public FunctionVariable(String name, Type type, boolean isConst) {
        super(name, type, new VoidValue(), isConst);
    }

    public FunctionVariable(FunctionVariable functionVariable) {
        super(functionVariable.getName(), functionVariable.getType(), new VoidValue(), functionVariable.isConst());
    }

    @Override
    public void setValue(Object value) {
        if (isConst && !(this.value instanceof VoidValue)) throw new RuntimeException(Main.getGlobal().getLineNumber(), "can't reassign the value of constant param");

        if (value instanceof VoidValue || type == Type.ANY || Type.isType(value, type)) {
            this.value = value;
            return;
        }

        throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + type + " value but found " + Type.getType(value));
    }

    public void clearValue() {
        value = new VoidValue();
    }
}
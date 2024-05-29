package me.itzisonn_.cmv.lang.main.functions.strings;

import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.Function;
import me.itzisonn_.cmv.lang.main.FunctionVariable;
import me.itzisonn_.cmv.lang.main.functions.DefaultFunction;
import me.itzisonn_.cmv.lang.main.functions.print.PrintlnFunction;
import me.itzisonn_.cmv.lang.types.Type;

import java.util.ArrayList;
import java.util.List;

public class SetCharAtFunction extends DefaultFunction {
    public SetCharAtFunction() {
        super("setCharAt", new ArrayList<>(List.of(
                new FunctionVariable("string", Type.STRING, true),
                new FunctionVariable("pos", Type.INT, true),
                new FunctionVariable("char", Type.STRING, true))), Type.STRING);
    }

    @Override
    public Function copy() {
        return new SetCharAtFunction();
    }

    @Override
    public Object runWithReturn(ArrayList<Object> paramsValues) {
        checkValues(paramsValues);

        if (paramsValues.get(0).toString().length() < Integer.parseInt(paramsValues.get(1).toString()) + 1) {
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "index out of bounds");
        }

        if (paramsValues.get(2).toString().length() != 1) {
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "argument at position 3 must be char");
        }

        return paramsValues.get(0).toString().substring(0, Integer.parseInt(paramsValues.get(1).toString())) +
                paramsValues.get(2) +
                paramsValues.get(0).toString().substring(Integer.parseInt(paramsValues.get(1).toString()) + 1);
    }
}
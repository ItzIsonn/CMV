package me.itzisonn_.cmv.lang.main.functions.strings;

import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.FunctionVariable;
import me.itzisonn_.cmv.lang.main.functions.DefaultFunction;
import me.itzisonn_.cmv.lang.types.Type;

import java.util.ArrayList;
import java.util.List;

public class GetCharAtFunction extends DefaultFunction {
    public GetCharAtFunction() {
        super("getCharAt", new ArrayList<>(List.of(
                new FunctionVariable("string", Type.STRING, true),
                new FunctionVariable("pos", Type.INT, true))), Type.STRING);
    }

    @Override
    public Object runWithReturn(ArrayList<Object> paramsValues) {
        checkValues(paramsValues);

        if (paramsValues.get(0).toString().length() < Integer.parseInt(paramsValues.get(1).toString()) + 1) {
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "index out of bounds");
        }

        return String.valueOf(paramsValues.get(0).toString().charAt(Integer.parseInt(paramsValues.get(1).toString())));
    }
}
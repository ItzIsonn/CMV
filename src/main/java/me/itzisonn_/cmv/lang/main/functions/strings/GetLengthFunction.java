package me.itzisonn_.cmv.lang.main.functions.strings;

import me.itzisonn_.cmv.lang.main.FunctionVariable;
import me.itzisonn_.cmv.lang.main.functions.DefaultFunction;
import me.itzisonn_.cmv.lang.types.Type;

import java.util.ArrayList;
import java.util.List;

public class GetLengthFunction extends DefaultFunction {
    public GetLengthFunction() {
        super("getLength", new ArrayList<>(List.of(
                new FunctionVariable("string", Type.STRING, true))), Type.INT);
    }

    @Override
    public Object runWithReturn(ArrayList<Object> paramsValues) {
        checkValues(paramsValues);

        return paramsValues.getFirst().toString().length();
    }
}
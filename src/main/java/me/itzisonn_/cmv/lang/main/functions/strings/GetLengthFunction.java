package me.itzisonn_.cmv.lang.main.functions.strings;

import me.itzisonn_.cmv.lang.main.Function;
import me.itzisonn_.cmv.lang.main.FunctionVariable;
import me.itzisonn_.cmv.lang.main.functions.DefaultFunction;
import me.itzisonn_.cmv.lang.main.functions.print.PrintlnFunction;
import me.itzisonn_.cmv.lang.types.Type;

import java.util.ArrayList;
import java.util.List;

public class GetLengthFunction extends DefaultFunction {
    public GetLengthFunction() {
        super("getLength", new ArrayList<>(List.of(
                new FunctionVariable("string", Type.STRING, true))), Type.INT);
    }

    @Override
    public Function copy() {
        return new GetLengthFunction();
    }

    @Override
    public Object runWithReturn(ArrayList<Object> paramsValues) {
        checkValues(paramsValues);

        return paramsValues.getFirst().toString().length();
    }
}
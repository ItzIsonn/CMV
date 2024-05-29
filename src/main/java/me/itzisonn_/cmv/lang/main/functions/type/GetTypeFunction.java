package me.itzisonn_.cmv.lang.main.functions.type;

import me.itzisonn_.cmv.lang.main.FunctionVariable;
import me.itzisonn_.cmv.lang.main.functions.DefaultFunction;
import me.itzisonn_.cmv.lang.types.Type;

import java.util.ArrayList;
import java.util.List;

public class GetTypeFunction extends DefaultFunction {
    public GetTypeFunction() {
        super("getType", new ArrayList<>(List.of(
                new FunctionVariable("value", Type.ANY, true))), Type.STRING);
    }

    @Override
    public Object runWithReturn(ArrayList<Object> paramsValues) {
        checkValues(paramsValues);

        return Type.getType(paramsValues.getFirst()).name();
    }
}
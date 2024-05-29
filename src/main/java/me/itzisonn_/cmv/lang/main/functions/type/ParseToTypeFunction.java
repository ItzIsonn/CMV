package me.itzisonn_.cmv.lang.main.functions.type;

import me.itzisonn_.cmv.lang.main.FunctionVariable;
import me.itzisonn_.cmv.lang.main.functions.DefaultFunction;
import me.itzisonn_.cmv.lang.types.Type;

import java.util.ArrayList;
import java.util.List;

public class ParseToTypeFunction extends DefaultFunction {
    public ParseToTypeFunction() {
        super("parseToType", new ArrayList<>(List.of(
                new FunctionVariable("value", Type.ANY, true),
                new FunctionVariable("type", Type.STRING, true))), Type.ANY);
    }

    @Override
    public Object runWithReturn(ArrayList<Object> paramsValues) {
        checkValues(paramsValues);

        return Type.parseValue(paramsValues.get(0), Type.ofString(paramsValues.get(1).toString()));
    }
}
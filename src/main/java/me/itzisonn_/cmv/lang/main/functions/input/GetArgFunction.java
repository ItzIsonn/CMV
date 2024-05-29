package me.itzisonn_.cmv.lang.main.functions.input;

import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.main.Function;
import me.itzisonn_.cmv.lang.main.functions.DefaultFunction;
import me.itzisonn_.cmv.lang.main.functions.print.PrintlnFunction;
import me.itzisonn_.cmv.lang.types.Type;

import java.util.ArrayList;

public class GetArgFunction extends DefaultFunction {
    public GetArgFunction() {
        super("getArg", new ArrayList<>(), Type.STRING);
    }

    @Override
    public Function copy() {
        return new GetArgFunction();
    }

    @Override
    public Object runWithReturn(ArrayList<Object> paramsValues) {
        checkValues(paramsValues);

        return convertStatement("\"" + Utils.getScanner().next() + "\"");
    }
}
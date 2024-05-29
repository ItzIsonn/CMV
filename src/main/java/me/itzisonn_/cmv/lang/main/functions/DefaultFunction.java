package me.itzisonn_.cmv.lang.main.functions;

import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.Function;
import me.itzisonn_.cmv.lang.main.FunctionVariable;
import me.itzisonn_.cmv.lang.types.Type;
import me.itzisonn_.cmv.lang.types.VoidValue;

import java.util.ArrayList;

public abstract class DefaultFunction extends Function {
    public DefaultFunction(String name, ArrayList<FunctionVariable> params, Type returnType) {
        super(name, params, returnType, null);
        complete();
    }

    protected void checkValues(ArrayList<Object> paramsValues) {
        if (paramsValues.size() > params.size() || paramsValues.size() < params.size())
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + params.size() + " arguments but found " + paramsValues.size());

        for (int i = 0; i < params.size(); i++) {
            FunctionVariable variable = params.get(i);
            if (!(paramsValues.get(i) instanceof VoidValue) && variable.getType() != Type.ANY && !Type.isType(paramsValues.get(i), variable.getType())) {
                throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + variable.getType() + " value but found " + Type.getType(paramsValues.get(i)));
            }
        }
    }
}

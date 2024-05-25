package me.itzisonn_.cmv.lang.main.functions.input;

import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.Function;

import java.util.ArrayList;

public class GetArgFunction extends Function {
    public GetArgFunction() {
        super("getArg", new ArrayList<>(), null);
    }

    @Override
    public String runWithReturn(ArrayList<String> paramsValues) {
        if (paramsValues.size() > paramsNames.size() || paramsValues.size() < paramsNames.size())
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + paramsNames.size() + " arguments but found " + paramsValues.size());

        return convertStatement("\"" + Utils.getScanner().next() + "\"");
    }
}
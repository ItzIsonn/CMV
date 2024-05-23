package me.itzisonn_.cmv.lang.main.functions.strings;

import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.Function;

import java.util.ArrayList;
import java.util.List;

public class SetCharAtFunction extends Function {
    public SetCharAtFunction() {
        super("setCharAt", new ArrayList<>(List.of("string", "pos", "char")), null);
        complete();
    }

    @Override
    public String runWithReturn(ArrayList<String> paramsValues) {
        if (paramsValues.size() > paramsNames.size() || paramsValues.size() < paramsNames.size())
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + paramsNames.size() + " arguments but found " + paramsValues.size());


        if (!Utils.isInt(paramsValues.get(1))) {
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "argument at position 2 must be integer");
        }

        if (paramsValues.get(0).length() < Integer.parseInt(paramsValues.get(1)) + 1) {
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "index out of bounds");
        }

        if (paramsValues.get(2).length() != 1) {
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "argument at position 3 must be char");
        }


        return paramsValues.get(0).substring(0, Integer.parseInt(paramsValues.get(1))) +
                paramsValues.get(2) +
                paramsValues.get(0).substring(Integer.parseInt(paramsValues.get(1)) + 1);
    }
}
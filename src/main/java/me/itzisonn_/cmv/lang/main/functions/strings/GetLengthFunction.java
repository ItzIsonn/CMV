package me.itzisonn_.cmv.lang.main.functions.strings;

import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.Function;

import java.util.ArrayList;
import java.util.List;

public class GetLengthFunction extends Function {
    public GetLengthFunction() {
        super("getLength", new ArrayList<>(List.of("string")), null);
        complete();
    }

    @Override
    public String runWithReturn(ArrayList<String> paramsValues) {
        if (paramsValues.size() > paramsNames.size() || paramsValues.size() < paramsNames.size())
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + paramsNames.size() + " arguments but found " + paramsValues.size());

        return String.valueOf(paramsValues.getFirst().length());
    }
}
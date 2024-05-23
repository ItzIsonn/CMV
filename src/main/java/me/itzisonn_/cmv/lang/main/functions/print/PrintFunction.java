package me.itzisonn_.cmv.lang.main.functions.print;

import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.Function;

import java.util.ArrayList;
import java.util.List;

public class PrintFunction extends Function {
    public PrintFunction() {
        super("print", new ArrayList<>(List.of("value")), null);
        complete();
    }

    @Override
    public void run(ArrayList<String> paramsValues) {
        if (paramsValues.size() > paramsNames.size() || paramsValues.size() < paramsNames.size())
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + paramsNames.size() + " arguments but found " + paramsValues.size());

        System.out.print(paramsValues.getFirst());
    }
}
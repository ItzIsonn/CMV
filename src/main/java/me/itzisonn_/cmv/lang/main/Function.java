package me.itzisonn_.cmv.lang.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.types.VoidType;
import me.itzisonn_.cmv.run.CompletableHandler;
import me.itzisonn_.cmv.run.Handler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = false)
public class Function extends CompletableHandler {
    private Object returnValue = new VoidType();
    private boolean isReturning = false;
    @Getter
    private final String name;
    @Getter
    protected final ArrayList<String> paramsNames;

    public Function(String name, ArrayList<String> paramsNames, Handler parent) {
        super(new ArrayList<>(), Main.getGlobal() == null ? 0 : Main.getGlobal().getLineNumber(), parent);
        if (!name.matches("[a-zA-Z_]*")) throw new RuntimeException(lineNumber, "function's name can only contain English alphabet's letters and underscores");
        this.name = name;
        this.paramsNames = paramsNames;

        handles.add(this::handleReturn);
    }

    @Override
    public void run() {
        for (String line : body) {
            if (isReturning) return;

            lineNumber++;
            if (!line.isEmpty() && !line.startsWith("//")) {
                this.line = line;
                isHandled = false;
                for (Runnable runnable : handles) {
                    if (!isHandled) runnable.run();
                }
                if (!isHandled) throw new RuntimeException(lineNumber, "unknown statement");
            }
        }

        if (bracketIndex > 0) {
            throw new RuntimeException(lineNumber, "can't find close-bracket");
        }
    }

    public void run(ArrayList<String> paramsValues) {
        if (paramsValues.size() > paramsNames.size() || paramsValues.size() < paramsNames.size())
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + paramsNames.size() + " arguments but found " + paramsValues.size());

        for (int i = 0; i < paramsNames.size(); i++) {
            Variable variable = new Variable(paramsNames.get(i), paramsValues.get(i), false);
            variables.add(variable);
        }

        run();
    }

    public String runWithReturn(ArrayList<String> paramsValues) {
        run(paramsValues);

        if (new VoidType().equals(returnValue))
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "function \"" + name + "\" returns nothing");
        return String.valueOf(returnValue);
    }

    protected void handleReturn() {
        isHandled = true;

        if (line.startsWith("return")) {
            if (line.equals("return")) {
                isReturning = true;
                return;
            }
            if (line.matches("return .+")) {
                Matcher matcher = Pattern.compile("return (.*)").matcher(line);

                if (matcher.find()) returnValue = convertStatement(matcher.group(1).trim());
                else throw new RuntimeException(lineNumber, "can't find return's value");

                isReturning = true;
                return;
            }

            throw new RuntimeException(lineNumber, "incorrect introduce to return statement");
        }

        isHandled = false;
    }
}
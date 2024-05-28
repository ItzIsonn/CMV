package me.itzisonn_.cmv.lang.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.types.Type;
import me.itzisonn_.cmv.lang.types.VoidValue;
import me.itzisonn_.cmv.run.CompletableHandler;
import me.itzisonn_.cmv.run.Handler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = false)
public class Function extends CompletableHandler {
    private Object returnValue = new VoidValue();
    private final Type returnType;
    private boolean isReturning = false;
    @Getter
    private final String name;
    @Getter
    protected final ArrayList<FunctionVariable> params;

    public Function(String name, ArrayList<FunctionVariable> params, Type returnType, Handler parent) {
        super(new ArrayList<>(), Main.getGlobal() == null ? 0 : Main.getGlobal().getLineNumber(), parent);
        if (!name.matches("[a-zA-Z_]*")) throw new RuntimeException(lineNumber, "function's name can only contain English alphabet's letters and underscores");
        this.name = name;
        this.params = params;
        this.returnType = returnType;

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

    public void run(ArrayList<Object> paramsValues) {
        if (paramsValues.size() > params.size() || paramsValues.size() < params.size())
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + params.size() + " arguments but found " + paramsValues.size());

        for (int i = 0; i < params.size(); i++) {
            FunctionVariable variable = params.get(i);
            variable.setValue(paramsValues.get(i));
            variables.add(variable);
        }

        run();

        for (FunctionVariable variable : params) {
            variable.clearValue();
        }

        if (returnType != null && returnValue instanceof VoidValue)
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "missing return statement in function \"" + name + "\"");
    }

    public Object runWithReturn(ArrayList<Object> paramsValues) {
        run(paramsValues);

        if (returnType == null)
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "function \"" + name + "\" returns nothing");

        if (returnValue instanceof VoidValue)
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "missing return statement in function \"" + name + "\"");

        return returnValue;
    }

    protected void handleReturn() {
        isHandled = true;

        if (line.startsWith("return")) {
            if (line.equals("return")) {
                if (returnType != null) {
                    throw new RuntimeException(lineNumber, "function must return a value");
                }

                isReturning = true;
                return;
            }

            if (line.matches("return .+")) {
                if (returnType == null) {
                    throw new RuntimeException(lineNumber, "function mustn't return a value");
                }

                Matcher matcher = Pattern.compile("return (.+)").matcher(line);

                if (matcher.find()) returnValue = convertStatement(matcher.group(1).trim());
                else throw new RuntimeException(lineNumber, "can't find return's value");

                if (!Type.isType(returnValue, returnType)) {
                    throw new RuntimeException(lineNumber, "expected " + returnType + " return type but found " + Type.getType(returnValue));
                }

                isReturning = true;
                return;
            }

            throw new RuntimeException(lineNumber, "incorrect introduce to return statement");
        }

        isHandled = false;
    }
}
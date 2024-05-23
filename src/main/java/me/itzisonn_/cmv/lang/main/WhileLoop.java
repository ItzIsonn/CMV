package me.itzisonn_.cmv.lang.main;

import lombok.EqualsAndHashCode;
import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.run.CompletableHandler;
import me.itzisonn_.cmv.run.Handler;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = false)
public class WhileLoop extends CompletableHandler {
    private final int startLineNumber;
    private final String expression;
    public WhileLoop(String expression, Handler parent) {
        super(new ArrayList<>(), Main.getGlobal().getLineNumber(), parent);
        startLineNumber = lineNumber;
        this.expression = expression;
    }

    @Override
    public void run() {
        if (!Utils.isBool(convertStatement(expression))) {
            throw new RuntimeException(lineNumber, "expected boolean value");
        }

        while (Boolean.parseBoolean(convertStatement(expression))) {
            for (String line : body) {
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

            lineNumber = startLineNumber;
        }
    }
}
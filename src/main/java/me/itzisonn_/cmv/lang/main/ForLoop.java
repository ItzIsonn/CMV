package me.itzisonn_.cmv.lang.main;

import lombok.EqualsAndHashCode;
import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.run.CompletableHandler;
import me.itzisonn_.cmv.run.Handler;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = false)
public class ForLoop extends CompletableHandler {
    private final int startLineNumber;
    private final String init;
    private final String termination;
    private final String increment;
    public ForLoop(String init, String termination, String increment, Handler parent) {
        super(new ArrayList<>(), Main.getGlobal().getLineNumber(), parent);
        startLineNumber = lineNumber;
        this.init = init;
        this.termination = termination;
        this.increment = increment;
    }

    @Override
    public void run() {
        this.line = init;
        handleVariable();

        ArrayList<Variable> backupVariables = new ArrayList<>(variables);

        if (!Utils.isBool(convertStatement(termination))) {
            throw new RuntimeException(lineNumber, "expected boolean value in the termination section");
        }

        while (Boolean.parseBoolean(convertStatement(termination))) {
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

            variables.clear();
            variables.addAll(backupVariables);

            lineNumber = startLineNumber;
            this.line = increment;
            handleVariable();

            backupVariables = new ArrayList<>(variables);
        }
    }
}
package me.itzisonn_.cmv.lang.main;

import lombok.EqualsAndHashCode;
import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.run.CompletableHandler;
import me.itzisonn_.cmv.run.Handler;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = false)
public class IfCondition extends CompletableHandler {
    private final boolean bool;

    public IfCondition(String bool, Handler parent) {
        super(new ArrayList<>(), Main.getGlobal().getLineNumber(), parent);
        if (!Utils.isBool(bool)) throw new RuntimeException(lineNumber, "expected boolean value");
        this.bool = Boolean.parseBoolean(bool);
    }

    @Override
    public void run() {
        if (bool) {
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
        }

        if (bracketIndex > 0) {
            throw new RuntimeException(lineNumber, "can't find close-bracket");
        }
    }
}
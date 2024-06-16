package me.itzisonn_.cmv.lang.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.run.CompletableHandler;
import me.itzisonn_.cmv.run.Handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Getter
@EqualsAndHashCode(callSuper = false)
public class IfCondition extends CompletableHandler {
    private final LinkedHashMap<String, FinishableArrayList<String>> conditions = new LinkedHashMap<>();

    public IfCondition(Handler parent) {
        super(new ArrayList<>(), parent.getLineNumber(), parent);
    }

    @Override
    public void addLine(String line) {
        if (getUnfinished() != null) getUnfinished().add(line);
    }

    public FinishableArrayList<String> getUnfinished() {
        for (String condition : conditions.keySet()) {
            if (!conditions.get(condition).isFinished()) {
                return conditions.get(condition);
            }
        }

        return null;
    }

    public void finish() {
        for (String condition : conditions.keySet()) {
            if (!conditions.get(condition).isFinished()) {
                conditions.get(condition).finish();
                return;
            }
        }
    }

    @Override
    public void run() {
        for (String condition : conditions.keySet()) {
            if (!Utils.isBool(convertStatement(condition))) throw new RuntimeException(lineNumber, "expected boolean value");

            if (Boolean.parseBoolean(convertStatement(condition).toString())) {
                for (String line : conditions.get(condition)) {
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

                return;
            }
        }
    }
}
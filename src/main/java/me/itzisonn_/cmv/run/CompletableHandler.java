package me.itzisonn_.cmv.run;

import lombok.Getter;
import me.itzisonn_.cmv.lang.main.Variable;

import java.util.ArrayList;

@Getter
public abstract class CompletableHandler extends Handler {
    protected final Handler parent;
    protected boolean isCompleted = false;

    public CompletableHandler(ArrayList<String> body, int lineNumber, Handler parent) {
        super(body, lineNumber);
        this.parent = parent;
    }

    public void addLine(String line) {
        if (!isCompleted) body.add(line);
    }

    public void complete() {
        isCompleted = true;
    }

    @Override
    protected Variable getVariable(String name) {
        for (Variable variable : variables) {
            if (variable.getName().equals(name)) {
                return variable;
            }
        }

        if (parent != null) return parent.getVariable(name);
        return null;
    }

    @Override
    public ArrayList<Variable> getVariables() {
        ArrayList<Variable> toReturn = variables;
        if (parent != null) toReturn.addAll(parent.getVariables());

        return toReturn;
    }
}
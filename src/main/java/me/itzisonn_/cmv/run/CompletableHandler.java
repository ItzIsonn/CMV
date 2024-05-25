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
    public ArrayList<Variable> getVariables() {
        ArrayList<Variable> toReturn = new ArrayList<>(variables);
        if (parent != null) toReturn.addAll(parent.getVariables());

        return toReturn;
    }
}
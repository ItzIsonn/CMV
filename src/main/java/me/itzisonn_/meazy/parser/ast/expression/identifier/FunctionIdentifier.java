package me.itzisonn_.meazy.parser.ast.expression.identifier;

import lombok.Getter;
import me.itzisonn_.meazy.parser.ast.expression.Expression;

import java.util.ArrayList;

@Getter
public class FunctionIdentifier extends Identifier {
    private final ArrayList<Expression> args;

    public FunctionIdentifier(String id, ArrayList<Expression> args) {
        super(id);
        this.args = args;
    }

    @Override
    public String toCodeString() {
        return id;
    }
}
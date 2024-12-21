package me.itzisonn_.meazy.parser.ast.expression.call_expression;

import lombok.Getter;
import me.itzisonn_.meazy.parser.ast.expression.Expression;

import java.util.ArrayList;

@Getter
public abstract class CallExpression implements Expression {
    protected final Expression caller;
    protected final ArrayList<Expression> args;

    public CallExpression(Expression caller, ArrayList<Expression> args) {
        this.caller = caller;
        this.args = args;
    }
}
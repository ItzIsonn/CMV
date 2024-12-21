package me.itzisonn_.meazy.parser.ast.expression.call_expression;

import me.itzisonn_.meazy.parser.ast.expression.Expression;

import java.util.ArrayList;

public class FunctionCallExpression extends CallExpression {
    public FunctionCallExpression(Expression caller, ArrayList<Expression> args) {
        super(caller, args);
    }

    @Override
    public String toCodeString() {
        StringBuilder argsBuilder = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            argsBuilder.append(args.get(i).toCodeString());
            if (i != args.size() - 1) argsBuilder.append(", ");
        }

        return caller.toCodeString() + "(" + argsBuilder + ")";
    }
}
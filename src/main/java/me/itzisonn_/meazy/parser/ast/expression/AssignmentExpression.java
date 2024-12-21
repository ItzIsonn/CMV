package me.itzisonn_.meazy.parser.ast.expression;

import lombok.Getter;

@Getter
public class AssignmentExpression implements Expression {
    private final Expression id;
    private final Expression value;

    public AssignmentExpression(Expression id, Expression value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toCodeString() {
        return id.toCodeString() + " = " + value.toCodeString();
    }
}

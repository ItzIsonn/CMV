package me.itzisonn_.meazy.parser.ast.expression;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberExpression implements Expression {
    private Expression object;
    private final Expression field;

    public MemberExpression(Expression object, Expression field) {
        this.object = object;
        this.field = field;
    }

    @Override
    public String toCodeString() {
        return object.toCodeString() + "." + field.toCodeString();
    }
}
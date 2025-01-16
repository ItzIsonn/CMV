package me.itzisonn_.meazy.parser.ast.statement;

import lombok.Getter;
import me.itzisonn_.meazy.parser.ast.expression.Expression;

@Getter
public class ReturnStatement implements Statement {
    private final Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
    }

    @Override
    public String toCodeString(int offset) throws IllegalArgumentException {
        if (value == null) return "return";
        return "return " + value.toCodeString(0);
    }
}

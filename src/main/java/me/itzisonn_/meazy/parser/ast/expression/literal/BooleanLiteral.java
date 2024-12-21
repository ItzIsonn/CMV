package me.itzisonn_.meazy.parser.ast.expression.literal;

import lombok.Getter;
import me.itzisonn_.meazy.parser.ast.expression.Expression;

@Getter
public class BooleanLiteral implements Expression {
    private final boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public String toCodeString() {
        return String.valueOf(value);
    }
}

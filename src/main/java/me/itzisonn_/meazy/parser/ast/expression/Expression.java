package me.itzisonn_.meazy.parser.ast.expression;

import me.itzisonn_.meazy.parser.ast.statement.Statement;

public interface Expression extends Statement {
    String toCodeString();

    @Override
    default String toCodeString(int offset) {
        return toCodeString();
    }
}

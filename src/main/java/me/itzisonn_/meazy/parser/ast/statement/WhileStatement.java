package me.itzisonn_.meazy.parser.ast.statement;

import lombok.Getter;
import me.itzisonn_.meazy.Utils;
import me.itzisonn_.meazy.parser.ast.expression.Expression;

import java.util.ArrayList;

@Getter
public class WhileStatement implements Statement {
    private final Expression condition;
    private final ArrayList<Statement> body;

    public WhileStatement(Expression condition, ArrayList<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toCodeString(int offset) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (Statement statement : body) {
            bodyBuilder.append(Utils.getOffset(offset)).append(statement.toCodeString(offset + 1)).append("\n");
        }

        return "while (" + condition.toCodeString(0) + ") {\n" + bodyBuilder + Utils.getOffset(offset - 1) + "}";
    }
}

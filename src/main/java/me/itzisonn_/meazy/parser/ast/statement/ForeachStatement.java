package me.itzisonn_.meazy.parser.ast.statement;

import lombok.Getter;
import me.itzisonn_.meazy.Utils;
import me.itzisonn_.meazy.parser.ast.expression.Expression;

import java.util.List;

@Getter
public class ForeachStatement implements Statement {
    private final VariableDeclarationStatement variableDeclarationStatement;
    private final Expression collection;
    private final List<Statement> body;

    public ForeachStatement(VariableDeclarationStatement variableDeclarationStatement, Expression collection, List<Statement> body) {
        this.variableDeclarationStatement = variableDeclarationStatement;
        this.collection = collection;
        this.body = body;
    }

    @Override
    public String toCodeString(int offset) {
        String variableDeclarationString = variableDeclarationStatement == null ? "" : variableDeclarationStatement.toCodeString(0);

        StringBuilder bodyBuilder = new StringBuilder();
        for (Statement statement : body) {
            bodyBuilder.append(Utils.getOffset(offset)).append(statement.toCodeString(offset + 1)).append("\n");
        }

        return "for (" + variableDeclarationString + " in " + collection.toCodeString(0) + ") {\n" + bodyBuilder + Utils.getOffset(offset - 1) + "}";
    }
}

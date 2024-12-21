package me.itzisonn_.meazy.parser.ast.statement;

import lombok.Getter;
import me.itzisonn_.meazy.Utils;
import me.itzisonn_.meazy.parser.ast.expression.AssignmentExpression;
import me.itzisonn_.meazy.parser.ast.expression.Expression;

import java.util.ArrayList;

@Getter
public class ForStatement implements Statement {
    private final VariableDeclarationStatement variableDeclarationStatement;
    private final Expression condition;
    private final AssignmentExpression assignmentExpression;
    private final ArrayList<Statement> body;

    public ForStatement(VariableDeclarationStatement variableDeclarationStatement, Expression condition, AssignmentExpression assignmentExpression, ArrayList<Statement> body) {
        this.variableDeclarationStatement = variableDeclarationStatement;
        this.condition = condition;
        this.assignmentExpression = assignmentExpression;
        this.body = body;
    }

    @Override
    public String toCodeString(int offset) {
        String variableDeclarationString = variableDeclarationStatement == null ? "" : variableDeclarationStatement.toCodeString(0);
        String conditionString = condition == null ? "" : " " + condition.toCodeString(0);
        String assignmentExpressionString = assignmentExpression == null ? "" : " " + assignmentExpression.toCodeString(0);

        StringBuilder bodyBuilder = new StringBuilder();
        for (Statement statement : body) {
            bodyBuilder.append(Utils.getOffset(offset)).append(statement.toCodeString(offset + 1)).append("\n");
        }

        return "for (" + variableDeclarationString + ";" + conditionString + ";" + assignmentExpressionString + ") {\n" + bodyBuilder + Utils.getOffset(offset - 1) + "}";
    }
}

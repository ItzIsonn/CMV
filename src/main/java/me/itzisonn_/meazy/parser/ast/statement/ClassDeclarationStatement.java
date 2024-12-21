package me.itzisonn_.meazy.parser.ast.statement;

import lombok.Getter;
import me.itzisonn_.meazy.Utils;

import java.util.ArrayList;

@Getter
public class ClassDeclarationStatement implements Statement {
    private final String id;
    private final ArrayList<Statement> body;

    public ClassDeclarationStatement(String id, ArrayList<Statement> body) {
        this.id = id;
        this.body = body;
    }

    @Override
    public String toCodeString(int offset) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (Statement statement : body) {
            bodyBuilder.append(Utils.getOffset(offset)).append(statement.toCodeString(offset + 1)).append("\n");
        }

        return "class " + id + " {\n" + bodyBuilder + Utils.getOffset(offset -1) + "}";
    }
}

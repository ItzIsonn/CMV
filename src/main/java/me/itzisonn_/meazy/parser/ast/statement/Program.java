package me.itzisonn_.meazy.parser.ast.statement;

import lombok.Getter;
import me.itzisonn_.meazy.Utils;

import java.util.ArrayList;

@Getter
public class Program implements Statement {
    private final String version;
    private final ArrayList<Statement> body;

    public Program(String version, ArrayList<Statement> body) {
        this.version = version;
        this.body = body;
    }

    @Override
    public String toCodeString(int offset) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < body.size(); i++) {
            bodyBuilder.append(Utils.getOffset(offset)).append(body.get(i).toCodeString(offset + 1));
            if (i != body.size() - 1) bodyBuilder.append("\n");
        }

        return bodyBuilder.toString();
    }
}
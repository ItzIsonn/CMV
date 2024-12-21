package me.itzisonn_.meazy.parser.ast.expression.identifier;

public class VariableIdentifier extends Identifier {
    public VariableIdentifier(String id) {
        super(id);
    }

    @Override
    public String toCodeString() {
        return id;
    }
}
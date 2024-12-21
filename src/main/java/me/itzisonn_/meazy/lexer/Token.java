package me.itzisonn_.meazy.lexer;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Token {
    private final int line;
    private final TokenType type;
    private final String value;

    public Token(int line, TokenType type, String value) {
        this.line = line;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token(" + line + "," + type + "," + value.replaceAll("\n", "\\\\n") + ")";
    }
}
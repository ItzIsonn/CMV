package me.itzisonn_.meazy.lexer;

import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode
public class TokenTypeSet {
    private final Set<TokenType> tokenTypes;

    public TokenTypeSet(Set<TokenType> tokenTypes) {
        this.tokenTypes = tokenTypes;
    }

    public TokenTypeSet(TokenType... tokenTypes) {
        this.tokenTypes = Set.of(tokenTypes);
    }

    public Set<TokenType> getTokenTypes() {
        return Set.copyOf(tokenTypes);
    }

    public boolean contains(TokenType tokenType) {
        return tokenTypes.contains(tokenType);
    }



    public static final TokenTypeSet KEYWORDS = new TokenTypeSet(
            TokenType.VARIABLE,
            TokenType.FUNCTION,
            TokenType.CLASS,
            TokenType.CONSTRUCTOR,
            TokenType.NEW,
            TokenType.PRIVATE,
            TokenType.SHARED,
            TokenType.IF,
            TokenType.ELSE,
            TokenType.FOR,
            TokenType.WHILE,
            TokenType.RETURN,
            TokenType.CONTINUE,
            TokenType.BREAK,
            TokenType.NULL,
            TokenType.BOOLEAN);

    public static final TokenTypeSet OPERATOR_ASSIGN = new TokenTypeSet(
            TokenType.PLUS_ASSIGN,
            TokenType.MINUS_ASSIGN,
            TokenType.MULTIPLY_ASSIGN,
            TokenType.DIVIDE_ASSIGN,
            TokenType.PERCENT_ASSIGN,
            TokenType.POWER_ASSIGN);

    public static final TokenTypeSet OPERATOR_POSTFIX = new TokenTypeSet(
            TokenType.DOUBLE_PLUS,
            TokenType.DOUBLE_MINUS);

    public static final TokenTypeSet ACCESS_MODIFIERS = new TokenTypeSet(
            TokenType.PRIVATE,
            TokenType.SHARED);

    public static void INIT() {}
}
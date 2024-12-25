package me.itzisonn_.meazy.lexer;

import me.itzisonn_.meazy.Utils;
import me.itzisonn_.meazy.registry.Registries;
import me.itzisonn_.meazy.registry.RegistryEntry;
import me.itzisonn_.meazy.registry.RegistryIdentifier;

import java.util.regex.Pattern;

public class TokenTypes {
    private static boolean isInit = false;

    private TokenTypes() {}



    public static TokenType VARIABLE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("variable")).getValue();
    }

    public static TokenType FUNCTION() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("function")).getValue();
    }

    public static TokenType CLASS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("class")).getValue();
    }

    public static TokenType CONSTRUCTOR() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("constructor")).getValue();
    }

    public static TokenType NEW() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("new")).getValue();
    }

    public static TokenType PRIVATE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("private")).getValue();
    }

    public static TokenType SHARED() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("shared")).getValue();
    }

    public static TokenType IF() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("if")).getValue();
    }

    public static TokenType ELSE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("else")).getValue();
    }

    public static TokenType FOR() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("for")).getValue();
    }

    public static TokenType IN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("in")).getValue();
    }

    public static TokenType WHILE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("while")).getValue();
    }

    public static TokenType RETURN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("return")).getValue();
    }

    public static TokenType CONTINUE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("continue")).getValue();
    }

    public static TokenType BREAK() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("break")).getValue();
    }



    public static TokenType NEW_LINE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("new_line")).getValue();
    }

    public static TokenType COMMENT() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("comment")).getValue();
    }

    public static TokenType MULTI_LINE_COMMENT() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("multi_line_comment")).getValue();
    }

    public static TokenType WHITE_SPACE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("white_space")).getValue();
    }

    public static TokenType END_OF_FILE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("end_of_file")).getValue();
    }



    public static TokenType LEFT_PAREN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("left_paren")).getValue();
    }

    public static TokenType RIGHT_PAREN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("right_paren")).getValue();
    }

    public static TokenType LEFT_BRACE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("left_brace")).getValue();
    }

    public static TokenType RIGHT_BRACE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("right_brace")).getValue();
    }

    public static TokenType LEFT_BRACKET() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("left_bracket")).getValue();
    }

    public static TokenType RIGHT_BRACKET() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("right_bracket")).getValue();
    }

    public static TokenType COLON() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("colon")).getValue();
    }

    public static TokenType SEMICOLON() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("semicolon")).getValue();
    }

    public static TokenType COMMA() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("comma")).getValue();
    }

    public static TokenType DOT() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("dot")).getValue();
    }



    public static TokenType ASSIGN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("assign")).getValue();
    }

    public static TokenType PLUS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("plus")).getValue();
    }

    public static TokenType MINUS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("minus")).getValue();
    }

    public static TokenType MULTIPLY() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("multiply")).getValue();
    }

    public static TokenType DIVIDE() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("divide")).getValue();
    }

    public static TokenType PERCENT() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("percent")).getValue();
    }

    public static TokenType POWER() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("power")).getValue();
    }

    public static TokenType PLUS_ASSIGN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("plus_assign")).getValue();
    }

    public static TokenType MINUS_ASSIGN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("minus_assign")).getValue();
    }

    public static TokenType MULTIPLY_ASSIGN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("multiply_assign")).getValue();
    }

    public static TokenType DIVIDE_ASSIGN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("divide_assign")).getValue();
    }

    public static TokenType PERCENT_ASSIGN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("percent_assign")).getValue();
    }

    public static TokenType POWER_ASSIGN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("power_assign")).getValue();
    }

    public static TokenType DOUBLE_PLUS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("double_plus")).getValue();
    }

    public static TokenType DOUBLE_MINUS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("double_minus")).getValue();
    }



    public static TokenType AND() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("and")).getValue();
    }

    public static TokenType OR() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("or")).getValue();
    }

    public static TokenType EQUALS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("equals")).getValue();
    }

    public static TokenType NOT_EQUALS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("not_equals")).getValue();
    }

    public static TokenType GREATER() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("greater")).getValue();
    }

    public static TokenType GREATER_OR_EQUALS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("greater_or_equals")).getValue();
    }

    public static TokenType LESS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("less")).getValue();
    }

    public static TokenType LESS_OR_EQUALS() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("less_or_equals")).getValue();
    }



    public static TokenType ID() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("id")).getValue();
    }

    public static TokenType NULL() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("null")).getValue();
    }

    public static TokenType NUMBER() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("number")).getValue();
    }

    public static TokenType STRING() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("string")).getValue();
    }

    public static TokenType BOOLEAN() {
        return Registries.TOKEN_TYPE.getEntry(RegistryIdentifier.ofDefault("boolean")).getValue();
    }



    /**
     * Returns TokenType whose pattern matches given string
     *
     * @param string String to match
     * @return TokenType or null if none matched
     * @throws NullPointerException When given string is null
     * @see Registries#TOKEN_TYPE
     */
    public static TokenType parse(String string) throws NullPointerException {
        if (string == null) throw new NullPointerException("String can't be null");

        for (RegistryEntry<TokenType> entry : Registries.TOKEN_TYPE.getEntries()) {
            TokenType tokenType = entry.getValue();
            Pattern pattern = tokenType.getPattern();
            if (pattern != null && pattern.matcher(string).matches() && tokenType.getCanMatch().test(string)) return tokenType;
        }

        return null;
    }



    private static void register(TokenType tokenType) {
        Registries.TOKEN_TYPE.register(RegistryIdentifier.ofDefault(tokenType.getId()), tokenType);
    }

    public static void INIT() {
        if (isInit) throw new IllegalStateException("TokenTypes already initialized!");
        isInit = true;

        register(new TokenType("variable", "var|val", false));
        register(new TokenType("function", "fn|func|function", false));
        register(new TokenType("class", "class", false));
        register(new TokenType("constructor", "constructor", false));
        register(new TokenType("new", "new", false));
        register(new TokenType("private", "private", false));
        register(new TokenType("shared", "shared", false));
        register(new TokenType("if", "if", false));
        register(new TokenType("else", "else", false));
        register(new TokenType("for", "for", false));
        register(new TokenType("in", "in", false));
        register(new TokenType("while", "while", false));
        register(new TokenType("return", "return", false));
        register(new TokenType("continue", "continue", false));
        register(new TokenType("break", "break", false));

        register(new TokenType("new_line", "\n", false));
        register(new TokenType("comment", "\\/\\/[^\n]*", true));
        register(new TokenType("multi_line_comment", "\\/\\*(?:(?!\\*\\/).)*\\*\\/", true));
        register(new TokenType("white_space", "(?!\n)\\s", true));
        register(new TokenType("end_of_file", null, false));

        register(new TokenType("left_paren", "\\(", false));
        register(new TokenType("right_paren", "\\)", false));
        register(new TokenType("left_brace", "\\{", false));
        register(new TokenType("right_brace", "\\}", false));
        register(new TokenType("left_bracket", "\\[", false));
        register(new TokenType("right_bracket", "\\]", false));
        register(new TokenType("colon", ":", false));
        register(new TokenType("semicolon", ";", false));
        register(new TokenType("comma", ",", false));
        register(new TokenType("dot", "\\.", false));

        register(new TokenType("assign", "=", false));
        register(new TokenType("plus", "\\+", false));
        register(new TokenType("minus", "-", false));
        register(new TokenType("multiply", "\\*", false));
        register(new TokenType("divide", "/", false));
        register(new TokenType("percent", "%", false));
        register(new TokenType("power", "\\^", false));
        register(new TokenType("plus_assign", "\\+=", false));
        register(new TokenType("minus_assign", "-=", false));
        register(new TokenType("multiply_assign", "\\*=", false));
        register(new TokenType("divide_assign", "/=", false));
        register(new TokenType("percent_assign", "%=", false));
        register(new TokenType("power_assign", "\\^=", false));
        register(new TokenType("double_plus", "\\+\\+", false));
        register(new TokenType("double_minus", "--", false));

        register(new TokenType("and", "&&", false));
        register(new TokenType("or", "\\|\\|", false));
        register(new TokenType("equals", "==", false));
        register(new TokenType("not_equals", "!=", false));
        register(new TokenType("greater", ">", false));
        register(new TokenType("greater_or_equals", ">=", false));
        register(new TokenType("less", "<", false));
        register(new TokenType("less_or_equals", "<=", false));

        register(new TokenType("null", "null", false));
        register(new TokenType("number", "(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?", false));
        register(new TokenType("string", "\"[^\"]*\"", false));
        register(new TokenType("boolean", "true|false", false));
        register(new TokenType("id", Utils.IDENTIFIER_REGEX, false, s -> {
            for (TokenType tokenType : TokenTypeSets.KEYWORDS.getTokenTypes()) {
                if (tokenType.getPattern().matcher(s).matches()) return false;
            }
            return true;
        }));
    }
}

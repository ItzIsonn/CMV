package me.itzisonn_.meazy.lexer;

import me.itzisonn_.meazy.Utils;
import me.itzisonn_.meazy.registry.Registries;
import me.itzisonn_.meazy.registry.RegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BasicLexer implements Lexer {
    public ArrayList<Token> parseTokens(String lines) {
        ArrayList<Token> tokens = new ArrayList<>();
        TokenType tokenType;
        TokenType lastMatched = null;
        int lineNumber = 1;

        for (int i = 0; i < lines.length(); i++) {
            if (i == lines.length() - 1) {
                tokenType = getMatchingToken(String.valueOf(lines.charAt(i)));
                if (tokenType != null && !tokenType.isShouldSkip()) tokens.add(new Token(lineNumber, tokenType, String.valueOf(lines.charAt(i))));
                break;
            }

            String lastString = "";
            int lastFound = -1;
            for (int j = i; j < lines.length(); j++) {
                lastString += lines.charAt(j);
                tokenType = getMatchingToken(lastString);
                if (tokenType != null) {
                    lastFound = j + 1;
                    lastMatched = tokenType;
                }
            }

            if (lastFound == -1) {
                throw new UnknownTokenException("At line " + lineNumber + ": " + lastString.replaceAll("\n", "\\\\n"));
            }

            if (!lastMatched.isShouldSkip()) tokens.add(new Token(lineNumber, lastMatched, lines.substring(i, lastFound)));
            if (lastMatched == TokenType.NEW_LINE) lineNumber++;
            else if (lastMatched == TokenType.MULTI_LINE_COMMENT) lineNumber += Utils.countMatches(lines.substring(i, lastFound), "\n");
            i = lastFound - 1;
            lastMatched = null;
        }
        tokens.add(new Token(lineNumber, TokenType.END_OF_FILE, ""));

        ArrayList<Token> newTokens = new ArrayList<>(List.of(tokens.getFirst()));
        for (int i = 1; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType().equals(TokenType.NEW_LINE) && tokens.get(i - 1).getType().equals(TokenType.NEW_LINE)) continue;
            newTokens.add(token);
        }

        return newTokens;
    }

    private TokenType getMatchingToken(String string) {
        for (RegistryEntry<TokenType> entry : Registries.TOKEN_TYPE.getEntries()) {
            String regex = entry.getValue().getRegex();
            if (regex != null && Pattern.compile(regex, Pattern.DOTALL).matcher(string).matches()) return entry.getValue();
        }

        return null;
    }
}
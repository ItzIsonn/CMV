package me.itzisonn_.meazy.lexer;

import java.util.ArrayList;

public interface Lexer {
    ArrayList<Token> parseTokens(String lines);
}
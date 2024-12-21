package me.itzisonn_.meazy.parser;

import me.itzisonn_.meazy.parser.ast.statement.Program;
import me.itzisonn_.meazy.lexer.Token;

import java.util.ArrayList;

public interface Parser {
    Program produceAST(ArrayList<Token> tokens);
}
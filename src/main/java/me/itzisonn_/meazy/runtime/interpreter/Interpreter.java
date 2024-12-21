package me.itzisonn_.meazy.runtime.interpreter;

import me.itzisonn_.meazy.parser.ast.statement.Program;

public interface Interpreter {
    void run(Program program);
}
package me.itzisonn_.meazy.registry;

import me.itzisonn_.meazy.parser.Parser;
import me.itzisonn_.meazy.lexer.Lexer;
import me.itzisonn_.meazy.lexer.TokenType;
import me.itzisonn_.meazy.parser.ast.DataType;
import me.itzisonn_.meazy.runtime.environment.interfaces.*;
import me.itzisonn_.meazy.runtime.environment.interfaces.declaration.VariableDeclarationEnvironment;
import me.itzisonn_.meazy.runtime.interpreter.Interpreter;

public class Registries {
    public static final SingleRegistry<Lexer> LEXER = new SingleRegistry<>();
    public static final SingleRegistry<Parser> PARSER = new SingleRegistry<>();
    public static final SingleRegistry<Interpreter> INTERPRETER = new SingleRegistry<>();

    public static final SingleRegistry<GlobalEnvironment> GLOBAL_ENVIRONMENT = new SingleRegistry<>();
    public static final SingleRegistry<Class<? extends ClassEnvironment>> CLASS_ENVIRONMENT = new SingleRegistry<>();
    public static final SingleRegistry<Class<? extends FunctionEnvironment>> FUNCTION_ENVIRONMENT = new SingleRegistry<>();
    public static final SingleRegistry<Class<? extends VariableDeclarationEnvironment>> VARIABLE_DECLARATION_ENVIRONMENT = new SingleRegistry<>();
    public static final SingleRegistry<Class<? extends LoopEnvironment>> LOOP_ENVIRONMENT = new SingleRegistry<>();
    public static final SingleRegistry<Class<? extends Environment>> ENVIRONMENT = new SingleRegistry<>();

    public static final MultipleRegistry<TokenType> TOKEN_TYPE = new MultipleRegistry<>();
    public static final MultipleRegistry<DataType> DATA_TYPE = new MultipleRegistry<>();
}
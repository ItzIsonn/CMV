package me.itzisonn_.meazy.parser.json_converters.statement;

import com.google.gson.*;
import me.itzisonn_.meazy.parser.ast.statement.Statement;
import me.itzisonn_.meazy.parser.ast.expression.Expression;
import me.itzisonn_.meazy.parser.ast.statement.*;
import me.itzisonn_.meazy.parser.json_converters.Converter;
import me.itzisonn_.meazy.parser.json_converters.InvalidCompiledFileException;

import java.lang.reflect.Type;

public class StatementConverter implements Converter<Statement> {
    @Override
    public Statement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        if (object.get("type") != null) {
            return jsonDeserializationContext.deserialize(jsonElement, switch (object.get("type").getAsString()) {
                case "break_statement" -> BreakStatement.class;
                case "class_declaration_statement" -> ClassDeclarationStatement.class;
                case "constructor_declaration_statement" -> ConstructorDeclarationStatement.class;
                case "continue_statement" -> ContinueStatement.class;
                case "for_statement" -> ForStatement.class;
                case "function_declaration_statement" -> FunctionDeclarationStatement.class;
                case "if_statement" -> IfStatement.class;
                case "program" -> Program.class;
                case "return_statement" -> ReturnStatement.class;
                case "variable_declaration_statement" -> VariableDeclarationStatement.class;
                case "while_statement" -> WhileStatement.class;
                default -> Expression.class;
            });
        }

        throw new InvalidCompiledFileException("Can't deserialize Statement because specified type is null");
    }

    @Override
    public JsonElement serialize(Statement statement, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(statement, statement.getClass());
    }
}
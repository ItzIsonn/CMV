package me.itzisonn_.meazy.parser.json_converters.expression;

import com.google.gson.*;
import me.itzisonn_.meazy.parser.ast.expression.*;
import me.itzisonn_.meazy.parser.ast.expression.call_expression.ClassCallExpression;
import me.itzisonn_.meazy.parser.ast.expression.call_expression.FunctionCallExpression;
import me.itzisonn_.meazy.parser.ast.expression.identifier.ClassIdentifier;
import me.itzisonn_.meazy.parser.ast.expression.identifier.FunctionIdentifier;
import me.itzisonn_.meazy.parser.ast.expression.identifier.VariableIdentifier;
import me.itzisonn_.meazy.parser.ast.expression.literal.BooleanLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.NullLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.NumberLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.StringLiteral;
import me.itzisonn_.meazy.parser.json_converters.Converter;
import me.itzisonn_.meazy.parser.json_converters.InvalidCompiledFileException;

import java.lang.reflect.Type;

public class ExpressionConverter implements Converter<Expression> {
    @Override
    public Expression deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        if (object.get("type") != null) {
            return jsonDeserializationContext.deserialize(jsonElement, switch (object.get("type").getAsString()) {
                case "assignment_expression" -> AssignmentExpression.class;
                case "binary_expression" -> BinaryExpression.class;
                case "call_arg_expression" -> CallArgExpression.class;
                case "comparison_expression" -> ComparisonExpression.class;
                case "logical_expression" -> LogicalExpression.class;
                case "member_expression" -> MemberExpression.class;
                case "class_call_expression" -> ClassCallExpression.class;
                case "function_call_expression" -> FunctionCallExpression.class;
                case "class_identifier" -> ClassIdentifier.class;
                case "function_identifier" -> FunctionIdentifier.class;
                case "variable_identifier" -> VariableIdentifier.class;
                case "boolean_literal" -> BooleanLiteral.class;
                case "null_literal" -> NullLiteral.class;
                case "number_literal" -> NumberLiteral.class;
                case "string_literal" -> StringLiteral.class;
                default -> throw new InvalidCompiledFileException("Can't deserialize Expression because specified type is invalid");
            });
        }

        throw new InvalidCompiledFileException("Can't deserialize Expression because specified type is null");
    }

    @Override
    public JsonElement serialize(Expression expression, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(expression, expression.getClass());
    }
}
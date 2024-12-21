package me.itzisonn_.meazy.parser.json_converters.statement;

import com.google.gson.*;
import me.itzisonn_.meazy.parser.ast.statement.Statement;
import me.itzisonn_.meazy.parser.ast.expression.Expression;
import me.itzisonn_.meazy.parser.ast.statement.WhileStatement;
import me.itzisonn_.meazy.parser.json_converters.Converter;
import me.itzisonn_.meazy.parser.json_converters.InvalidCompiledFileException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WhileStatementConverter implements Converter<WhileStatement> {
    @Override
    public WhileStatement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        if (object.get("type") != null && object.get("type").getAsString().equals("while_statement")) {
            if (object.get("condition") == null) throw new InvalidCompiledFileException("WhileStatement doesn't have field condition");
            Expression condition = jsonDeserializationContext.deserialize(object.get("condition"), Expression.class);

            if (object.get("body") == null) throw new InvalidCompiledFileException("WhileStatement doesn't have field body");
            ArrayList<Statement> body = new ArrayList<>();
            for (JsonElement statement : object.get("body").getAsJsonArray()) {
                body.add(jsonDeserializationContext.deserialize(statement, Statement.class));
            }

            return new WhileStatement(condition, body);
        }

        throw new InvalidCompiledFileException("Can't deserialize WhileStatement because specified type is null or doesn't match");
    }

    @Override
    public JsonElement serialize(WhileStatement whileStatement, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("type", "while_statement");

        result.add("condition", jsonSerializationContext.serialize(whileStatement.getCondition()));

        JsonArray body = new JsonArray();
        for (Statement statement : whileStatement.getBody()) {
            body.add(jsonSerializationContext.serialize(statement));
        }
        result.add("body", body);

        return result;
    }
}
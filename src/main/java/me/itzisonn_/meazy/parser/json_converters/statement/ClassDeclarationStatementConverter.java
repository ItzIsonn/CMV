package me.itzisonn_.meazy.parser.json_converters.statement;

import com.google.gson.*;
import me.itzisonn_.meazy.parser.ast.statement.Statement;
import me.itzisonn_.meazy.parser.ast.statement.ClassDeclarationStatement;
import me.itzisonn_.meazy.parser.json_converters.Converter;
import me.itzisonn_.meazy.parser.json_converters.InvalidCompiledFileException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ClassDeclarationStatementConverter implements Converter<ClassDeclarationStatement> {
    @Override
    public ClassDeclarationStatement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        if (object.get("type") != null && object.get("type").getAsString().equals("class_declaration_statement")) {
            if (object.get("id") == null) throw new InvalidCompiledFileException("ClassDeclarationStatement doesn't have field id");
            String id = object.get("id").getAsString();

            if (object.get("body") == null) throw new InvalidCompiledFileException("ClassDeclarationStatement doesn't have field body");
            ArrayList<Statement> body = new ArrayList<>();
            for (JsonElement statement : object.get("body").getAsJsonArray()) {
                body.add(jsonDeserializationContext.deserialize(statement, Statement.class));
            }

            return new ClassDeclarationStatement(id, body);
        }

        throw new InvalidCompiledFileException("Can't deserialize ClassDeclarationStatement because specified type is null or doesn't match");
    }

    @Override
    public JsonElement serialize(ClassDeclarationStatement classDeclarationStatement, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("type", "class_declaration_statement");

        result.addProperty("id", classDeclarationStatement.getId());

        JsonArray body = new JsonArray();
        for (Statement statement : classDeclarationStatement.getBody()) {
            body.add(jsonSerializationContext.serialize(statement));
        }
        result.add("body", body);

        return result;
    }

    @Override
    public String getId() {
        return "class_declaration_statement";
    }
}
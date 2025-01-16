package me.itzisonn_.meazy.parser.json_converters.statement;

import com.google.gson.*;
import me.itzisonn_.meazy.parser.ast.statement.ContinueStatement;
import me.itzisonn_.meazy.parser.json_converters.Converter;
import me.itzisonn_.meazy.registry.RegistryIdentifier;

import java.lang.reflect.Type;

public class ContinueStatementConverter extends Converter<ContinueStatement> {
    public ContinueStatementConverter() {
        super(RegistryIdentifier.ofDefault("continue_statement"));
    }

    @Override
    public ContinueStatement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        checkType(object);

        return new ContinueStatement();
    }

    @Override
    public JsonElement serialize(ContinueStatement continueStatement, Type type, JsonSerializationContext jsonSerializationContext) {
        return getJsonObject();
    }
}
package me.itzisonn_.meazy.parser.ast;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.itzisonn_.meazy.parser.ast.expression.literal.BooleanLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.NumberLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.StringLiteral;
import me.itzisonn_.meazy.registry.Registries;
import me.itzisonn_.meazy.registry.RegistryIdentifier;
import me.itzisonn_.meazy.runtime.values.BooleanValue;
import me.itzisonn_.meazy.runtime.values.number.IntValue;
import me.itzisonn_.meazy.runtime.values.number.NumberValue;
import me.itzisonn_.meazy.runtime.values.StringValue;

import java.util.function.Predicate;

@Getter
@EqualsAndHashCode
public abstract class DataType {
    private final String name;

    public DataType(String name) {
        this.name = name;
    }

    public abstract boolean isMatches(Object value);



    public static final DataType ANY = register("any", "any", o -> true);

    public static final DataType BOOLEAN = register("boolean", "boolean", o -> {
        if (o instanceof Boolean || o instanceof BooleanLiteral || o instanceof BooleanValue) return true;
        return o.toString().equals("true") || o.toString().equals("false");
    });

    public static final DataType INT = register("int", "int", o -> {
        if (o instanceof Integer || o instanceof IntValue) return true;
        if (o instanceof NumberLiteral numberLiteral) {
            return numberLiteral.isInt();
        }
        try {
            Integer.parseInt(o.toString());
            return true;
        }
        catch (NumberFormatException ignore) {
            return false;
        }
    });

    public static final DataType FLOAT = register("float", "float", o -> {
        if (o instanceof Float || o instanceof NumberLiteral || o instanceof NumberValue) return true;
        try {
            Float.parseFloat(o.toString());
            return true;
        }
        catch (NumberFormatException ignore) {
            return false;
        }
    });

    public static final DataType STRING = register("string", "string", o ->
            o instanceof String || o instanceof StringLiteral || o instanceof StringValue);



    private static DataType register(String id, String name, Predicate<Object> predicate) {
        return Registries.DATA_TYPE.register(RegistryIdentifier.ofDefault(id), new DataType(name) {
            public boolean isMatches(Object value) {
                return predicate.test(value);
            }
        });
    }

    public static void INIT() {}
}
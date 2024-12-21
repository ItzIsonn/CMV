package me.itzisonn_.meazy.runtime.environment.basic.default_classes;

import me.itzisonn_.meazy.parser.ast.DataType;
import me.itzisonn_.meazy.parser.ast.expression.CallArgExpression;
import me.itzisonn_.meazy.runtime.environment.basic.BasicClassEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.Environment;
import me.itzisonn_.meazy.runtime.values.BooleanValue;
import me.itzisonn_.meazy.runtime.values.RuntimeValue;
import me.itzisonn_.meazy.runtime.values.StringValue;
import me.itzisonn_.meazy.runtime.values.clazz.ClassValue;
import me.itzisonn_.meazy.runtime.values.function.DefaultFunctionValue;
import me.itzisonn_.meazy.runtime.values.number.DoubleValue;
import me.itzisonn_.meazy.runtime.values.number.IntValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TypesClassEnvironment extends BasicClassEnvironment {
    public TypesClassEnvironment(Environment parent) {
        super(parent, true, "Types");


        declareFunction("getType", new DefaultFunctionValue(new ArrayList<>(List.of(new CallArgExpression("value", DataType.ANY, true))), DataType.STRING, this, Set.of("shared")) {
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionArgs.getFirst().getFinalValue();
                if (value == null) return new StringValue("null");
                if (DataType.BOOLEAN.isMatches(value)) return new StringValue(DataType.BOOLEAN.getName());
                if (DataType.INT.isMatches(value)) return new StringValue(DataType.INT.getName());
                if (DataType.FLOAT.isMatches(value)) return new StringValue(DataType.FLOAT.getName());
                if (DataType.STRING.isMatches(value)) return new StringValue(DataType.STRING.getName());
                if (functionArgs.getFirst().getFinalRuntimeValue() instanceof ClassValue classValue) return new StringValue(classValue.getId());
                return new StringValue("???");
            }
        });

        declareFunction("convert", new DefaultFunctionValue(
                new ArrayList<>(List.of(new CallArgExpression("value", DataType.ANY, true), new CallArgExpression("type", DataType.STRING, true))),
                DataType.ANY, this, Set.of("shared")) {
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                String type = functionArgs.get(1).getFinalValue().toString();

                Object value = functionArgs.getFirst().getFinalValue();
                if (value == null) throw new IllegalArgumentException("Can't parse value to type " + type);

                switch (type) {
                    case "any" -> {
                        return functionArgs.getFirst().getFinalRuntimeValue();
                    }
                    case "boolean" -> {
                        if (value.toString().equals("true") || value.toString().equals("false")) return new BooleanValue(Boolean.parseBoolean(value.toString()));
                        throw new IllegalArgumentException("Can't parse " + value + " to type " + type);
                    }
                    case "int" -> {
                        try {
                            return new IntValue(Integer.parseInt(value.toString().replaceAll("\\.0$", "")));
                        }
                        catch (NumberFormatException ignore) {
                            throw new IllegalArgumentException("Can't parse " + value + " to type " + type);
                        }
                    }
                    case "float" -> {
                        try {
                            return new DoubleValue(Double.parseDouble(value.toString()));
                        }
                        catch (NumberFormatException ignore) {
                            throw new IllegalArgumentException("Can't parse " + value + " to type " + type);
                        }
                    }
                    case "string" -> {
                        return new StringValue(value.toString());
                    }
                    default -> throw new IllegalArgumentException("Can only parse default types");
                }
            }
        });
    }
}

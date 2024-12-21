package me.itzisonn_.meazy.runtime.environment.basic.default_classes;

import me.itzisonn_.meazy.Utils;
import me.itzisonn_.meazy.parser.ast.DataType;
import me.itzisonn_.meazy.parser.ast.expression.CallArgExpression;
import me.itzisonn_.meazy.runtime.environment.basic.BasicClassEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.ClassEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.Environment;
import me.itzisonn_.meazy.runtime.interpreter.InvalidArgumentException;
import me.itzisonn_.meazy.runtime.interpreter.InvalidCallException;
import me.itzisonn_.meazy.runtime.interpreter.InvalidSyntaxException;
import me.itzisonn_.meazy.runtime.values.*;
import me.itzisonn_.meazy.runtime.values.clazz.DefaultClassValue;
import me.itzisonn_.meazy.runtime.values.clazz.constructor.DefaultConstructorValue;
import me.itzisonn_.meazy.runtime.values.function.DefaultFunctionValue;
import me.itzisonn_.meazy.runtime.values.number.IntValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StringClassEnvironment extends BasicClassEnvironment {
    public StringClassEnvironment(Environment parent) {
        super(parent, true, "String");


        declareVariable("value", DataType.STRING, new NullValue(), false, new HashSet<>());
        declareConstructor(new DefaultConstructorValue(new ArrayList<>(List.of(new CallArgExpression("value", DataType.STRING, true))), this, new HashSet<>()) {
            @Override
            public void run(ArrayList<RuntimeValue<?>> constructorArgs, Environment constructorEnvironment) {
                constructorEnvironment.getVariableEnvironment("value").assignVariable("value", constructorArgs.getFirst().getFinalRuntimeValue());
            }
        });


        declareFunction("getLength", new DefaultFunctionValue(new ArrayList<>(), DataType.INT, this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();
                if (value instanceof String string) {
                    return new IntValue(string.length());
                }
                throw new InvalidSyntaxException("Can't get length of non-string value");
            }
        });

        declareFunction("replace", new DefaultFunctionValue(
                new ArrayList<>(List.of(new CallArgExpression("target", DataType.STRING, true), new CallArgExpression("replacement", DataType.STRING, true))),
                Utils.parseDataType("String"), this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    if (!(getParentEnvironment() instanceof ClassEnvironment classEnvironment)) {
                        throw new InvalidCallException("Invalid function call");
                    }
                    functionEnvironment.getVariableEnvironment("value").assignVariable("value",
                            new StringValue(string.replace(functionArgs.getFirst().getFinalValue().toString(), functionArgs.get(1).getFinalValue().toString())));
                    return new DefaultClassValue(classEnvironment);
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("replaceRegex", new DefaultFunctionValue(
                new ArrayList<>(List.of(new CallArgExpression("target", DataType.STRING, true), new CallArgExpression("replacement", DataType.STRING, true))),
                Utils.parseDataType("String"), this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    if (!(getParentEnvironment() instanceof ClassEnvironment classEnvironment)) {
                        throw new InvalidCallException("Invalid function call");
                    }
                    functionEnvironment.getVariableEnvironment("value").assignVariable("value",
                            new StringValue(string.replaceAll(functionArgs.getFirst().getFinalValue().toString(), functionArgs.get(1).getFinalValue().toString())));
                    return new DefaultClassValue(classEnvironment);
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("replaceFirst", new DefaultFunctionValue(
                new ArrayList<>(List.of(new CallArgExpression("target", DataType.STRING, true), new CallArgExpression("replacement", DataType.STRING, true))),
                Utils.parseDataType("String"), this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    if (!(getParentEnvironment() instanceof ClassEnvironment classEnvironment)) {
                        throw new InvalidCallException("Invalid function call");
                    }
                    functionEnvironment.getVariableEnvironment("value").assignVariable("value",
                            new StringValue(string.replaceFirst(functionArgs.getFirst().getFinalValue().toString(), functionArgs.get(1).getFinalValue().toString())));
                    return new DefaultClassValue(classEnvironment);
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("toUpperCase", new DefaultFunctionValue(new ArrayList<>(), Utils.parseDataType("String"), this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    if (!(getParentEnvironment() instanceof ClassEnvironment classEnvironment)) {
                        throw new InvalidCallException("Invalid function call");
                    }
                    functionEnvironment.getVariableEnvironment("value").assignVariable("value", new StringValue(string.toUpperCase()));
                    return new DefaultClassValue(classEnvironment);
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("toLowerCase", new DefaultFunctionValue(new ArrayList<>(), Utils.parseDataType("String"), this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    if (!(getParentEnvironment() instanceof ClassEnvironment classEnvironment)) {
                        throw new InvalidCallException("Invalid function call");
                    }
                    functionEnvironment.getVariableEnvironment("value").assignVariable("value", new StringValue(string.toLowerCase()));
                    return new DefaultClassValue(classEnvironment);
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("getCharAt", new DefaultFunctionValue(new ArrayList<>(List.of(new CallArgExpression("pos", DataType.INT, true))), DataType.STRING, this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                int pos;
                try {
                    pos = Integer.parseInt(functionArgs.getFirst().getFinalValue().toString().replaceAll("\\.0$", ""));
                }
                catch (NumberFormatException ignore) {
                    throw new InvalidArgumentException("Position must be int");
                }

                if (value instanceof String string) {
                    try {
                        return new StringValue(String.valueOf(string.charAt(pos)));
                    }
                    catch (IndexOutOfBoundsException ignore) {
                        throw new InvalidArgumentException("Index " + pos + "out of bounds " + (string.length() - 1));
                    }
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("setCharAt", new DefaultFunctionValue(
                new ArrayList<>(List.of(new CallArgExpression("pos", DataType.INT, true), new CallArgExpression("char", DataType.STRING, true))),
                Utils.parseDataType("String"), this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                int pos;
                try {
                    pos = Integer.parseInt(functionArgs.getFirst().getFinalValue().toString().replaceAll("\\.0$", ""));
                }
                catch (NumberFormatException ignore) {
                    throw new InvalidArgumentException("Position must be int");
                }

                String stringChar = functionArgs.get(1).getFinalValue().toString();
                if (stringChar.length() != 1) {
                    throw new InvalidArgumentException("Char must be one character long");
                }
                char ch = stringChar.charAt(0);

                if (value instanceof String string) {
                    if (!(getParentEnvironment() instanceof ClassEnvironment classEnvironment)) {
                        throw new InvalidCallException("Invalid function call");
                    }
                    StringBuilder stringBuilder = new StringBuilder(string);
                    stringBuilder.setCharAt(pos, ch);
                    functionEnvironment.getVariableEnvironment("value").assignVariable("value", new StringValue(stringBuilder.toString()));
                    return new DefaultClassValue(classEnvironment);
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("contains", new DefaultFunctionValue(new ArrayList<>(List.of(new CallArgExpression("target", DataType.STRING, true))), DataType.BOOLEAN, this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    return new BooleanValue(string.contains(functionArgs.getFirst().getFinalValue().toString()));
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("repeat", new DefaultFunctionValue(new ArrayList<>(List.of(new CallArgExpression("count", DataType.INT, true))), Utils.parseDataType("String"), this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                int count;
                try {
                    count = Integer.parseInt(functionArgs.getFirst().getFinalValue().toString().replaceAll("\\.0$", ""));
                }
                catch (NumberFormatException ignore) {
                    throw new InvalidArgumentException("Count must be int");
                }

                if (value instanceof String string) {
                    if (!(getParentEnvironment() instanceof ClassEnvironment classEnvironment)) {
                        throw new InvalidCallException("Invalid function call");
                    }
                    functionEnvironment.getVariableEnvironment("value").assignVariable("value", new StringValue(string.repeat(count)));
                    return new DefaultClassValue(classEnvironment);
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("trim", new DefaultFunctionValue(new ArrayList<>(), Utils.parseDataType("String"), this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    if (!(getParentEnvironment() instanceof ClassEnvironment classEnvironment)) {
                        throw new InvalidCallException("Invalid function call");
                    }
                    functionEnvironment.getVariableEnvironment("value").assignVariable("value", new StringValue(string.trim()));
                    return new DefaultClassValue(classEnvironment);
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("startsWith", new DefaultFunctionValue(new ArrayList<>(List.of(new CallArgExpression("target", DataType.STRING, true))), DataType.BOOLEAN, this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    return new BooleanValue(string.startsWith(functionArgs.getFirst().getFinalValue().toString()));
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("endsWith", new DefaultFunctionValue(new ArrayList<>(List.of(new CallArgExpression("target", DataType.STRING, true))), DataType.BOOLEAN, this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    return new BooleanValue(string.endsWith(functionArgs.getFirst().getFinalValue().toString()));
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("isBlank", new DefaultFunctionValue(new ArrayList<>(), DataType.BOOLEAN, this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                if (value instanceof String string) {
                    return new BooleanValue(string.isBlank());
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });

        declareFunction("substring", new DefaultFunctionValue(
                new ArrayList<>(List.of(new CallArgExpression("begin", DataType.INT, true), new CallArgExpression("end", DataType.INT, true))),
                Utils.parseDataType("String"), this, new HashSet<>()) {
            @Override
            public RuntimeValue<?> run(ArrayList<RuntimeValue<?>> functionArgs, Environment functionEnvironment) {
                Object value = functionEnvironment.getVariableEnvironment("value").getVariable("value").getValue().getValue();

                int begin;
                try {
                    begin = Integer.parseInt(functionArgs.getFirst().getFinalValue().toString().replaceAll("\\.0$", ""));
                }
                catch (NumberFormatException ignore) {
                    throw new InvalidArgumentException("Begin must be int");
                }

                int end;
                try {
                    end = Integer.parseInt(functionArgs.get(1).getFinalValue().toString().replaceAll("\\.0$", ""));
                }
                catch (NumberFormatException ignore) {
                    throw new InvalidArgumentException("End must be int");
                }

                if (value instanceof String string) {
                    if (!(getParentEnvironment() instanceof ClassEnvironment classEnvironment)) {
                        throw new InvalidCallException("Invalid function call");
                    }
                    functionEnvironment.getVariableEnvironment("value").assignVariable("value", new StringValue(string.substring(begin, end)));
                    return new DefaultClassValue(classEnvironment);
                }
                throw new InvalidSyntaxException("Value must be string");
            }
        });
    }
}
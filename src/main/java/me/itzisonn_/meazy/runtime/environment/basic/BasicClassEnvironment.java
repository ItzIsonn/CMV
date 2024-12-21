package me.itzisonn_.meazy.runtime.environment.basic;

import lombok.Getter;
import me.itzisonn_.meazy.parser.ast.expression.CallArgExpression;
import me.itzisonn_.meazy.runtime.environment.RuntimeFunction;
import me.itzisonn_.meazy.runtime.environment.interfaces.ClassEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.Environment;
import me.itzisonn_.meazy.runtime.environment.interfaces.declaration.FunctionDeclarationEnvironment;
import me.itzisonn_.meazy.runtime.interpreter.InvalidSyntaxException;
import me.itzisonn_.meazy.runtime.interpreter.UnknownIdentifierException;
import me.itzisonn_.meazy.runtime.values.RuntimeValue;
import me.itzisonn_.meazy.runtime.values.clazz.constructor.ConstructorValue;
import me.itzisonn_.meazy.runtime.values.clazz.constructor.DefaultConstructorValue;
import me.itzisonn_.meazy.runtime.values.function.DefaultFunctionValue;
import me.itzisonn_.meazy.runtime.values.function.FunctionValue;

import java.util.ArrayList;

@Getter
public class BasicClassEnvironment extends BasicVariableDeclarationEnvironment implements ClassEnvironment {
    private final String id;
    private final ArrayList<RuntimeFunction> functions;
    private final ArrayList<RuntimeValue<?>> constructors;

    public BasicClassEnvironment(Environment parent, boolean isShared, String id) {
        super(parent, isShared);
        this.id = id;
        this.functions = new ArrayList<>();
        this.constructors = new ArrayList<>();
    }

    public BasicClassEnvironment(Environment parent, String id) {
        super(parent, false);
        this.id = id;
        this.functions = new ArrayList<>();
        this.constructors = new ArrayList<>();
    }

    public void declareFunction(String id, RuntimeValue<?> value) {
        ArrayList<CallArgExpression> args;
        if (value instanceof FunctionValue functionValue) args = functionValue.getArgs();
        else if (value instanceof DefaultFunctionValue defaultFunctionValue) args = defaultFunctionValue.getArgs();
        else return;

        main:
        for (RuntimeFunction runtimeFunction : functions) {
            if (runtimeFunction.getId().equals(id)) {
                ArrayList<CallArgExpression> callArgExpressions;
                if (runtimeFunction.getFunctionValue() instanceof FunctionValue functionValue) callArgExpressions = functionValue.getArgs();
                else if (runtimeFunction.getFunctionValue() instanceof DefaultFunctionValue defaultFunctionValue)
                    callArgExpressions = defaultFunctionValue.getArgs();
                else continue;

                if (args.size() != callArgExpressions.size()) continue;

                for (int i = 0; i < args.size(); i++) {
                    CallArgExpression callArgExpression = callArgExpressions.get(i);
                    if (!callArgExpression.getDataType().equals(args.get(i).getDataType())) continue main;
                }

                throw new InvalidSyntaxException("Function with id " + id + " already exists!");
            }
        }

        functions.add(new RuntimeFunction(id, value));
    }

    public FunctionDeclarationEnvironment getFunctionEnvironment(String id, ArrayList<RuntimeValue<?>> args) {
        if (getFunction(id, args) != null) return this;
        if (parent == null || !(parent instanceof FunctionDeclarationEnvironment functionDeclarationEnvironment))
            throw new UnknownIdentifierException("Function with id " + id + " doesn't exist!");
        return functionDeclarationEnvironment.getFunctionEnvironment(id, args);
    }

    public RuntimeValue<?> getFunction(String id, ArrayList<RuntimeValue<?>> args) {
        main:
        for (RuntimeFunction runtimeFunction : functions) {
            if (runtimeFunction.getId().equals(id)) {
                ArrayList<CallArgExpression> callArgExpressions;
                if (runtimeFunction.getFunctionValue() instanceof FunctionValue functionValue) callArgExpressions = functionValue.getArgs();
                else if (runtimeFunction.getFunctionValue() instanceof DefaultFunctionValue defaultFunctionValue)
                    callArgExpressions = defaultFunctionValue.getArgs();
                else continue;

                if (args.size() != callArgExpressions.size()) continue;

                for (int i = 0; i < args.size(); i++) {
                    CallArgExpression callArgExpression = callArgExpressions.get(i);
                    if (!callArgExpression.getDataType().isMatches(args.get(i))) continue main;
                }

                return runtimeFunction.getFunctionValue();
            }
        }

        return null;
    }

    public void declareConstructor(RuntimeValue<?> value) {
        ArrayList<CallArgExpression> args;
        if (value instanceof ConstructorValue constructorValue) args = constructorValue.getArgs();
        else if (value instanceof DefaultConstructorValue defaultConstructorValue) args = defaultConstructorValue.getArgs();
        else return;

        main:
        for (RuntimeValue<?> runtimeValue : constructors) {
            ArrayList<CallArgExpression> callArgExpressions;
            if (runtimeValue instanceof ConstructorValue constructorValue) callArgExpressions = constructorValue.getArgs();
            else if (runtimeValue instanceof DefaultConstructorValue defaultConstructorValue) callArgExpressions = defaultConstructorValue.getArgs();
            else continue;

            if (args.size() != callArgExpressions.size()) continue;

            for (int i = 0; i < args.size(); i++) {
                CallArgExpression callArgExpression = callArgExpressions.get(i);
                if (!callArgExpression.getDataType().equals(args.get(i).getDataType())) continue main;
            }

            throw new InvalidSyntaxException("Constructor with this args already exists!");
        }

        constructors.add(value);
    }

    public RuntimeValue<?> getConstructor(ArrayList<RuntimeValue<?>> args) {
        main:
        for (RuntimeValue<?> runtimeValue : constructors) {
            ArrayList<CallArgExpression> callArgExpressions;
            if (runtimeValue instanceof ConstructorValue constructorValue) callArgExpressions = constructorValue.getArgs();
            else if (runtimeValue instanceof DefaultConstructorValue defaultConstructorValue) callArgExpressions = defaultConstructorValue.getArgs();
            else continue;

            if (args.size() != callArgExpressions.size()) continue;

            for (int i = 0; i < args.size(); i++) {
                CallArgExpression callArgExpression = callArgExpressions.get(i);
                if (!callArgExpression.getDataType().isMatches(args.get(i))) continue main;
            }

            return runtimeValue;
        }

        return null;
    }

    public boolean hasConstructor() {
        return !constructors.isEmpty();
    }

    @Override
    public String getId() {
        return id;
    }
}
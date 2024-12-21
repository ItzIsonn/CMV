package me.itzisonn_.meazy.runtime.interpreter;

import me.itzisonn_.meazy.MeazyMain;
import me.itzisonn_.meazy.parser.ast.expression.*;
import me.itzisonn_.meazy.parser.ast.expression.call_expression.ClassCallExpression;
import me.itzisonn_.meazy.parser.ast.expression.call_expression.FunctionCallExpression;
import me.itzisonn_.meazy.parser.ast.expression.identifier.ClassIdentifier;
import me.itzisonn_.meazy.parser.ast.expression.identifier.FunctionIdentifier;
import me.itzisonn_.meazy.parser.ast.expression.identifier.Identifier;
import me.itzisonn_.meazy.parser.ast.expression.identifier.VariableIdentifier;
import me.itzisonn_.meazy.parser.ast.expression.literal.BooleanLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.NullLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.NumberLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.StringLiteral;
import me.itzisonn_.meazy.parser.ast.statement.*;
import me.itzisonn_.meazy.registry.Registries;
import me.itzisonn_.meazy.runtime.environment.RuntimeClass;
import me.itzisonn_.meazy.runtime.environment.RuntimeVariable;
import me.itzisonn_.meazy.runtime.environment.interfaces.ClassEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.Environment;
import me.itzisonn_.meazy.runtime.environment.interfaces.FunctionEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.LoopEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.declaration.ClassDeclarationEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.declaration.ConstructorDeclarationEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.declaration.FunctionDeclarationEnvironment;
import me.itzisonn_.meazy.runtime.environment.interfaces.declaration.VariableDeclarationEnvironment;
import me.itzisonn_.meazy.runtime.values.*;
import me.itzisonn_.meazy.runtime.values.clazz.ClassValue;
import me.itzisonn_.meazy.runtime.values.clazz.DefaultClassValue;
import me.itzisonn_.meazy.runtime.values.clazz.constructor.ConstructorValue;
import me.itzisonn_.meazy.runtime.values.clazz.constructor.DefaultConstructorValue;
import me.itzisonn_.meazy.runtime.values.function.DefaultFunctionValue;
import me.itzisonn_.meazy.runtime.values.function.FunctionValue;
import me.itzisonn_.meazy.runtime.values.VariableValue;
import me.itzisonn_.meazy.runtime.values.number.DoubleValue;
import me.itzisonn_.meazy.runtime.values.number.IntValue;
import me.itzisonn_.meazy.runtime.values.number.NumberValue;
import me.itzisonn_.meazy.runtime.values.statement_info.BreakInfoValue;
import me.itzisonn_.meazy.runtime.values.statement_info.ContinueInfoValue;
import me.itzisonn_.meazy.runtime.values.statement_info.ReturnInfoValue;
import org.apache.logging.log4j.Level;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BasicInterpreter implements Interpreter {
    @Override
    public void run(Program program) {
        evaluate(program, Registries.GLOBAL_ENVIRONMENT.getEntry().getValue());

        RuntimeValue<?> runtimeValue = Registries.GLOBAL_ENVIRONMENT.getEntry().getValue().getFunction("main", new ArrayList<>());
        if (runtimeValue == null) {
            MeazyMain.getInstance().getLogger().log(Level.WARN, "File doesn't contain main function");
            return;
        }

        if (runtimeValue instanceof FunctionValue functionValue) {
            FunctionEnvironment functionEnvironment;
            try {
                functionEnvironment = Registries.FUNCTION_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class).newInstance(Registries.GLOBAL_ENVIRONMENT.getEntry().getValue());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            if (!functionValue.getArgs().isEmpty()) throw new InvalidArgumentException("Main function must have no args");

            for (int i = 0; i < functionValue.getBody().size(); i++) {
                Statement statement = functionValue.getBody().get(i);
                if (statement instanceof ReturnStatement returnStatement) {
                    if (returnStatement.getValue() != null) {
                        throw new InvalidSyntaxException("Found return statement but function must return nothing");
                    }
                    if (i + 1 < functionValue.getBody().size()) throw new InvalidSyntaxException("Return statement must be last in body");
                    break;
                }
                RuntimeValue<?> value = evaluate(statement, functionEnvironment);
                if (value instanceof ReturnInfoValue returnInfoValue) {
                    if (returnInfoValue.getFinalValue() != null) {
                        throw new InvalidSyntaxException("Found return statement but function must return nothing");
                    }
                    break;
                }
            }
        }
        else MeazyMain.getInstance().getLogger().log(Level.WARN, "File contains invalid main function");
    }



    protected RuntimeValue<?> evaluate(Statement statement, Environment environment, Environment... extra) {
        return switch (statement) {
            case null -> throw new NullPointerException("Statement can't be null!");

            case Program program -> evaluateProgram(program, environment);

            case ClassDeclarationStatement classDeclarationStatement -> evaluateClassDeclaration(classDeclarationStatement, environment);
            case ConstructorDeclarationStatement constructorDeclarationStatement -> evaluateConstructorDeclaration(constructorDeclarationStatement, environment);
            case FunctionDeclarationStatement functionDeclarationStatement -> evaluateFunctionDeclaration(functionDeclarationStatement, environment);
            case VariableDeclarationStatement variableDeclarationStatement -> evaluateVariableDeclaration(variableDeclarationStatement, environment);
            case IfStatement ifStatement -> evaluateIfStatement(ifStatement, environment);
            case ForStatement forStatement -> evaluateForStatement(forStatement, environment);
            case WhileStatement whileStatement -> evaluateWhileStatement(whileStatement, environment);
            case ReturnStatement returnStatement -> evaluateReturnStatement(returnStatement, environment);
            case ContinueStatement ignore -> evaluateContinueStatement(environment);
            case BreakStatement ignore -> evaluateBreakStatement(environment);

            case AssignmentExpression assignmentExpression -> evaluateAssignmentExpression(assignmentExpression, environment);
            case LogicalExpression logicalExpression -> evaluateLogicalExpression(logicalExpression, environment);
            case ComparisonExpression comparisonExpression -> evaluateComparisonExpression(comparisonExpression, environment);
            case BinaryExpression binaryExpression -> evaluateBinaryExpression(binaryExpression, environment);
            case FunctionCallExpression functionCallExpression -> {
                if (extra.length > 0) yield evaluateFunctionCallExpression(functionCallExpression, environment, extra[0]);
                else yield evaluateFunctionCallExpression(functionCallExpression, environment, environment);
            }
            case ClassCallExpression classCallExpression -> {
                if (extra.length > 0) yield evaluateClassCallExpression(classCallExpression, environment, extra[0]);
                else yield evaluateClassCallExpression(classCallExpression, environment, environment);
            }
            case MemberExpression memberExpression -> evaluateMemberExpression(memberExpression, environment);

            case Identifier identifier -> {
                if (extra.length > 0) yield evaluateIdentifier(identifier, environment, extra[0]);
                else yield evaluateIdentifier(identifier, environment, environment);
            }
            case NullLiteral ignore -> new NullValue();
            case NumberLiteral numberLiteral -> {
                if (numberLiteral.isInt()) yield new IntValue((int) numberLiteral.getValue());
                else yield new DoubleValue(numberLiteral.getValue());
            }
            case StringLiteral stringLiteral -> new StringValue(stringLiteral.getValue());
            case BooleanLiteral booleanLiteral -> new BooleanValue(booleanLiteral.isValue());

            default -> throw new UnsupportedNodeException(statement.getClass().getName());
        };
    }



    protected RuntimeValue<?> evaluateProgram(Program program, Environment environment) {
        for (Statement statement : program.getBody()) {
            evaluate(statement, environment);
        }

        return null;
    }



    protected RuntimeValue<?> evaluateClassDeclaration(ClassDeclarationStatement classDeclarationStatement, Environment environment) {
        if (environment instanceof ClassDeclarationEnvironment classDeclarationEnvironment) {
            ClassEnvironment classEnvironment;
            try {
                classEnvironment = Registries.CLASS_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class, boolean.class, String.class).newInstance(Registries.GLOBAL_ENVIRONMENT.getEntry().getValue(), true, classDeclarationStatement.getId());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            for (Statement statement : classDeclarationStatement.getBody()) {
                evaluate(statement, classEnvironment);
            }

            ClassValue classValue = new ClassValue(classEnvironment, classDeclarationStatement.getBody());
            classDeclarationEnvironment.declareClass(classDeclarationStatement.getId(), classValue);
            return null;
        }

        throw new InvalidSyntaxException("Can't declare class in this environment!");
    }

    protected RuntimeValue<?> evaluateConstructorDeclaration(ConstructorDeclarationStatement constructorDeclarationStatement, Environment environment) {
        if (environment instanceof ConstructorDeclarationEnvironment constructorDeclarationEnvironment) {
            if (constructorDeclarationStatement.getAccessModifiers().contains("shared"))
                throw new InvalidSyntaxException("Constructors can't have shared access modifier!");

            ConstructorValue constructorValue = new ConstructorValue(
                    constructorDeclarationStatement.getArgs(),
                    constructorDeclarationStatement.getBody(),
                    constructorDeclarationEnvironment,
                    constructorDeclarationStatement.getAccessModifiers());

            constructorDeclarationEnvironment.declareConstructor(constructorValue);
            return null;
        }

        throw new InvalidSyntaxException("Can't declare constructor in this environment!");
    }

    protected RuntimeValue<?> evaluateFunctionDeclaration(FunctionDeclarationStatement functionDeclarationStatement, Environment environment) {
        if (environment instanceof FunctionDeclarationEnvironment functionDeclarationEnvironment) {
            FunctionValue functionValue = new FunctionValue(
                    functionDeclarationStatement.getArgs(),
                    functionDeclarationStatement.getBody(),
                    functionDeclarationStatement.getReturnDataType(),
                    functionDeclarationEnvironment,
                    functionDeclarationStatement.getAccessModifiers());

            functionDeclarationEnvironment.declareFunction(functionDeclarationStatement.getId(), functionValue);
            return null;
        }

        throw new InvalidSyntaxException("Can't declare function in this environment!");
    }

    protected RuntimeValue<?> evaluateVariableDeclaration(VariableDeclarationStatement variableDeclarationStatement, Environment environment) {
        if (!(environment instanceof VariableDeclarationEnvironment variableDeclarationEnvironment)) throw new InvalidSyntaxException("Can't declare variable in this environment!");
        Set<String> accessModifiers = new HashSet<>(variableDeclarationStatement.getAccessModifiers());
        if (!accessModifiers.contains("shared") && environment.isShared()) accessModifiers.add("shared");
        variableDeclarationEnvironment.declareVariable(
                variableDeclarationStatement.getId(),
                variableDeclarationStatement.getDataType(),
                new VariableValue(evaluate(variableDeclarationStatement.getValue(), environment), variableDeclarationEnvironment, variableDeclarationStatement.getId()),
                variableDeclarationStatement.isConstant(),
                accessModifiers);
        return null;
    }

    protected RuntimeValue<?> evaluateIfStatement(IfStatement ifStatement, Environment environment) {
        while (ifStatement != null) {
            if (ifStatement.getCondition() != null) {
                RuntimeValue<?> condition = evaluate(ifStatement.getCondition(), environment).getFinalRuntimeValue();
                if (!(condition instanceof BooleanValue booleanValue)) throw new InvalidArgumentException("If condition must be boolean value");

                if (!booleanValue.getValue()) {
                    ifStatement = ifStatement.getElseStatement();
                    continue;
                }
            }

            Environment ifEnvironment;
            try {
                ifEnvironment = Registries.VARIABLE_DECLARATION_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class).newInstance(environment);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < ifStatement.getBody().size(); i++) {
                Statement statement = ifStatement.getBody().get(i);
                RuntimeValue<?> result = evaluate(statement, ifEnvironment);

                if (statement instanceof ReturnStatement) {
                    if (i + 1 < ifStatement.getBody().size()) throw new InvalidSyntaxException("Return statement must be last in body");
                    return new ReturnInfoValue(result);
                }
                if (result instanceof ReturnInfoValue returnInfoValue) {
                    return returnInfoValue;
                }

                if (statement instanceof ContinueStatement) {
                    if (i + 1 < ifStatement.getBody().size()) throw new InvalidSyntaxException("Continue statement must be last in body");
                    return new ContinueInfoValue();
                }
                if (result instanceof ContinueInfoValue continueInfoValue) {
                    return continueInfoValue;
                }

                if (statement instanceof BreakStatement) {
                    if (i + 1 < ifStatement.getBody().size()) throw new InvalidSyntaxException("Break statement must be last in body");
                    return new BreakInfoValue();
                }
                if (result instanceof BreakInfoValue breakInfoValue) {
                    return breakInfoValue;
                }
            }
            break;
        }
        return null;
    }

    protected RuntimeValue<?> evaluateForStatement(ForStatement forStatement, Environment environment) {
        VariableDeclarationEnvironment forEnvironment;
        try {
            forEnvironment = Registries.LOOP_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class).newInstance(environment);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        forEnvironment.declareVariable(
                forStatement.getVariableDeclarationStatement().getId(),
                forStatement.getVariableDeclarationStatement().getDataType(),
                evaluate(forStatement.getVariableDeclarationStatement().getValue(), forEnvironment),
                forStatement.getVariableDeclarationStatement().isConstant(),
                new HashSet<>());

        main:
        while (parseCondition(forStatement.getCondition(), forEnvironment)) {
            for (int i = 0; i < forStatement.getBody().size(); i++) {
                Statement statement = forStatement.getBody().get(i);
                RuntimeValue<?> result = evaluate(statement, forEnvironment);

                if (statement instanceof ReturnStatement) {
                    if (i + 1 < forStatement.getBody().size()) throw new InvalidSyntaxException("Return statement must be last in body");
                    return new ReturnInfoValue(result);
                }
                if (result instanceof ReturnInfoValue returnInfoValue) {
                    return returnInfoValue;
                }

                if (statement instanceof ContinueStatement) {
                    if (i + 1 < forStatement.getBody().size()) throw new InvalidSyntaxException("Continue statement must be last in body");
                    break;
                }
                if (result instanceof ContinueInfoValue) {
                    break;
                }

                if (statement instanceof BreakStatement) {
                    if (i + 1 < forStatement.getBody().size()) throw new InvalidSyntaxException("Break statement must be last in body");
                    break main;
                }
                if (result instanceof BreakInfoValue) {
                    break main;
                }
            }

            RuntimeVariable runtimeVariable = forEnvironment.getVariable(forStatement.getVariableDeclarationStatement().getId());
            forEnvironment.clearVariables();
            forEnvironment.declareVariable(
                    runtimeVariable.getId(),
                    runtimeVariable.getDataType(),
                    runtimeVariable.getValue(),
                    runtimeVariable.isConstant(),
                    new HashSet<>());
            forEnvironment.assignVariable(runtimeVariable.getId(), evaluateAssignmentExpression(forStatement.getAssignmentExpression(), forEnvironment));
        }

        return null;
    }

    protected RuntimeValue<?> evaluateWhileStatement(WhileStatement whileStatement, Environment environment) {
        VariableDeclarationEnvironment whileEnvironment;
        try {
            whileEnvironment = Registries.LOOP_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class).newInstance(environment);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        main:
        while (parseCondition(whileStatement.getCondition(), environment)) {
            whileEnvironment.clearVariables();

            for (int i = 0; i < whileStatement.getBody().size(); i++) {
                Statement statement = whileStatement.getBody().get(i);
                RuntimeValue<?> result = evaluate(statement, whileEnvironment);

                if (statement instanceof ReturnStatement) {
                    if (i + 1 < whileStatement.getBody().size()) throw new InvalidSyntaxException("Return statement must be last in body");
                    return new ReturnInfoValue(result);
                }
                if (result instanceof ReturnInfoValue returnInfoValue) {
                    return returnInfoValue;
                }

                if (statement instanceof ContinueStatement) {
                    if (i + 1 < whileStatement.getBody().size()) throw new InvalidSyntaxException("Continue statement must be last in body");
                    break;
                }
                if (result instanceof ContinueInfoValue) {
                    break;
                }

                if (statement instanceof BreakStatement) {
                    if (i + 1 < whileStatement.getBody().size()) throw new InvalidSyntaxException("Break statement must be last in body");
                    break main;
                }
                if (result instanceof BreakInfoValue) {
                    break main;
                }
            }
        }

        return null;
    }

    protected boolean parseCondition(Expression rawCondition, Environment environment) {
        RuntimeValue<?> condition = evaluate(rawCondition, environment).getFinalRuntimeValue();

        if (!(condition instanceof BooleanValue booleanValue)) throw new InvalidArgumentException("Condition must be boolean value");
        return booleanValue.getValue();
    }

    protected RuntimeValue<?> evaluateReturnStatement(ReturnStatement returnStatement, Environment environment) {
        if (environment instanceof FunctionEnvironment || environment.hasParent(parent -> parent instanceof FunctionEnvironment)) {
            if (returnStatement.getValue() == null) return null;
            return evaluate(returnStatement.getValue(), environment);
        }
        if (returnStatement.getValue() == null &&
                (environment instanceof VariableDeclarationEnvironment || environment.hasParent(parent -> parent instanceof VariableDeclarationEnvironment))) {
            return null;
        }

        throw new InvalidSyntaxException("Can only use return statement inside a function or if/for/while statements");
    }

    protected RuntimeValue<?> evaluateContinueStatement(Environment environment) {
        if (environment instanceof LoopEnvironment || environment.hasParent(parent -> parent instanceof LoopEnvironment)) {
            return null;
        }

        throw new InvalidSyntaxException("Can only use continue statement inside for/while statements");
    }

    protected RuntimeValue<?> evaluateBreakStatement(Environment environment) {
        if (environment instanceof LoopEnvironment || environment.hasParent(parent -> parent instanceof LoopEnvironment)) {
            return null;
        }

        throw new InvalidSyntaxException("Can only use break statement inside for/while statements");
    }



    protected RuntimeValue<?> evaluateAssignmentExpression(AssignmentExpression assignmentExpression, Environment environment) {
        if (assignmentExpression.getId() instanceof VariableIdentifier variableIdentifier) {
            if (!(environment instanceof VariableDeclarationEnvironment variableDeclarationEnvironment)) {
                throw new InvalidSyntaxException("Can't assign value not in variable declaration environment");
            }
            RuntimeValue<?> value = evaluate(assignmentExpression.getValue(), environment);
            if (!(value instanceof VariableValue)) value = new VariableValue(value,
                    variableDeclarationEnvironment.getVariableEnvironment(variableIdentifier.getId()),
                    variableIdentifier.getId());
            variableDeclarationEnvironment.getVariableEnvironment(variableIdentifier.getId()).assignVariable(variableIdentifier.getId(), value);
            return value;
        }
        if (assignmentExpression.getId() instanceof MemberExpression memberExpression) {
            RuntimeValue<?> memberExpressionValue = evaluate(memberExpression, environment);
            if (memberExpressionValue instanceof VariableValue variableValue) {
                RuntimeValue<?> value = evaluate(assignmentExpression.getValue(), environment);
                if (!(value instanceof VariableValue)) value = new VariableValue(value, variableValue.getParentEnvironment(), variableValue.getId());
                variableValue.getParentEnvironment().assignVariable(variableValue.getId(), value);
                return value;
            }
            throw new InvalidSyntaxException("Can't assign value to not variable");
        }
        return null;
    }

    protected RuntimeValue<?> evaluateLogicalExpression(LogicalExpression logicalExpression, Environment environment) {
        RuntimeValue<?> left = evaluate(logicalExpression.getLeft(), environment).getFinalRuntimeValue();
        RuntimeValue<?> right = evaluate(logicalExpression.getRight(), environment).getFinalRuntimeValue();

        if (left instanceof BooleanValue leftValue && right instanceof BooleanValue rightValue) {
            boolean result;
            boolean leftBoolean = leftValue.getValue();
            boolean rightBoolean = rightValue.getValue();

            switch (logicalExpression.getOperator()) {
                case "&&" -> result = leftBoolean && rightBoolean;
                case "||" -> result = leftBoolean || rightBoolean;
                default -> throw new UnsupportedOperatorException(logicalExpression.getOperator());
            }

            return new BooleanValue(result);
        }

        throw new InvalidSyntaxException("Logical expression must contain only boolean values");
    }

    protected RuntimeValue<?> evaluateComparisonExpression(ComparisonExpression comparisonExpression, Environment environment) {
        RuntimeValue<?> left = evaluate(comparisonExpression.getLeft(), environment).getFinalRuntimeValue();
        RuntimeValue<?> right = evaluate(comparisonExpression.getRight(), environment).getFinalRuntimeValue();

        if (left instanceof NumberValue<?> leftValue && right instanceof NumberValue<?> rightValue) {
            boolean result;
            double leftNumber = leftValue.getValue().doubleValue();
            double rightNumber = rightValue.getValue().doubleValue();

            switch (comparisonExpression.getOperator()) {
                case "==" -> result = leftNumber == rightNumber;
                case "!=" -> result = leftNumber != rightNumber;
                case ">" -> result = leftNumber > rightNumber;
                case ">=" -> result = leftNumber >= rightNumber;
                case "<" -> result = leftNumber < rightNumber;
                case "<=" -> result = leftNumber <= rightNumber;
                default -> throw new UnsupportedOperatorException(comparisonExpression.getOperator());
            }

            return new BooleanValue(result);
        }

        if (left instanceof StringValue leftValue && right instanceof StringValue rightValue) {
            boolean result;
            String leftNumber = leftValue.getValue();
            String rightNumber = rightValue.getValue();

            switch (comparisonExpression.getOperator()) {
                case "==" -> result = leftNumber.equals(rightNumber);
                case "!=" -> result = !leftNumber.equals(rightNumber);
                case ">" -> result = leftNumber.length() > rightNumber.length();
                case ">=" -> result = leftNumber.length() >= rightNumber.length();
                case "<" -> result = leftNumber.length() < rightNumber.length();
                case "<=" -> result = leftNumber.length() <= rightNumber.length();
                default -> throw new UnsupportedOperatorException(comparisonExpression.getOperator());
            }

            return new BooleanValue(result);
        }

        if (left instanceof BooleanValue leftValue && right instanceof BooleanValue rightValue) {
            boolean result;
            boolean leftBoolean = leftValue.getValue();
            boolean rightBoolean = rightValue.getValue();

            switch (comparisonExpression.getOperator()) {
                case "==" -> result = leftBoolean == rightBoolean;
                case "!=" -> result = leftBoolean != rightBoolean;
                default -> throw new UnsupportedOperatorException(comparisonExpression.getOperator());
            }

            return new BooleanValue(result);
        }

        if (left instanceof NullValue) {
            boolean result;
            Object rightObject = right.getValue();

            switch (comparisonExpression.getOperator()) {
                case "==" -> result = null == rightObject;
                case "!=" -> result = null != rightObject;
                default -> throw new UnsupportedOperatorException(comparisonExpression.getOperator());
            }

            return new BooleanValue(result);
        }

        if (right instanceof NullValue) {
            boolean result;
            Object leftObject = left.getValue();

            switch (comparisonExpression.getOperator()) {
                case "==" -> result = leftObject == null;
                case "!=" -> result = leftObject != null;
                default -> throw new UnsupportedOperatorException(comparisonExpression.getOperator());
            }

            return new BooleanValue(result);
        }

        throw new InvalidSyntaxException("Can't compare two different values" + left + " " + right);
    }

    protected RuntimeValue<?> evaluateBinaryExpression(BinaryExpression binaryExpression, Environment environment) {
        RuntimeValue<?> left = evaluate(binaryExpression.getLeft(), environment).getFinalRuntimeValue();
        RuntimeValue<?> right = evaluate(binaryExpression.getRight(), environment).getFinalRuntimeValue();

        if (left instanceof NumberValue<?> leftValue && right instanceof NumberValue<?> rightValue) {
            return evaluateNumericBinaryExpression(leftValue, rightValue, binaryExpression.getOperator());
        }
        else {
            return evaluateStringBinaryExpression(left, right, binaryExpression.getOperator());
        }
    }

    protected RuntimeValue<?> evaluateFunctionCallExpression(FunctionCallExpression functionCallExpression, Environment lookupEnvironment, Environment argsEnvironment) {
        ArrayList<RuntimeValue<?>> args = new ArrayList<>();
        functionCallExpression.getArgs().forEach(arg -> args.add(evaluate(arg, argsEnvironment).getFinalRuntimeValue()));

        RuntimeValue<?> function = evaluate(functionCallExpression.getCaller(), lookupEnvironment, argsEnvironment);

        if (function instanceof DefaultFunctionValue defaultFunctionValue) {
            if (defaultFunctionValue.getArgs().size() != args.size()) {
                throw new InvalidCallException("Expected " + defaultFunctionValue.getArgs().size() + " args but found " + args.size());
            }

            FunctionEnvironment functionEnvironment;
            try {
                functionEnvironment = Registries.FUNCTION_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class).newInstance(defaultFunctionValue.getParentEnvironment());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            RuntimeValue<?> returnValue = defaultFunctionValue.run(args, functionEnvironment);
            if (defaultFunctionValue.getReturnDataType() != null && !defaultFunctionValue.getReturnDataType().isMatches(returnValue))
                throw new InvalidSyntaxException("Returned value's data type is different from specified. It's probably an addon's error");
            return returnValue;
        }
        if (function instanceof FunctionValue functionValue) {
            if (functionValue.getArgs().size() != args.size()) {
                throw new InvalidCallException("Expected " + functionValue.getArgs().size() + " args but found " + args.size());
            }

            FunctionEnvironment functionEnvironment;
            try {
                functionEnvironment = Registries.FUNCTION_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class).newInstance(functionValue.getParentEnvironment());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < functionValue.getArgs().size(); i++) {
                CallArgExpression callArgExpression = functionValue.getArgs().get(i);
                functionEnvironment.declareArgument(
                        callArgExpression.getId(),
                        callArgExpression.getDataType(),
                        args.get(i),
                        callArgExpression.isConstant(),
                        new HashSet<>());
            }

            RuntimeValue<?> result = null;
            boolean hasReturnStatement = false;
            for (int i = 0; i < functionValue.getBody().size(); i++) {
                Statement statement = functionValue.getBody().get(i);
                if (statement instanceof ReturnStatement) {
                    hasReturnStatement = true;
                    result = evaluate(statement, functionEnvironment);
                    if (result != null) {
                        if (functionValue.getReturnDataType() != null && !functionValue.getReturnDataType().isMatches(result.getFinalValue()))
                            throw new InvalidSyntaxException("Returned value's data type is different from specified (" + functionValue.getReturnDataType().getName() + ")");
                        if (functionValue.getReturnDataType() == null)
                            throw new InvalidSyntaxException("Found return statement but function must return nothing");
                    }
                    if (i + 1 < functionValue.getBody().size()) throw new InvalidSyntaxException("Return statement must be last in body");
                    break;
                }
                RuntimeValue<?> value = evaluate(statement, functionEnvironment);
                if (value instanceof ReturnInfoValue returnInfoValue) {
                    hasReturnStatement = true;
                    result = returnInfoValue.getFinalRuntimeValue();
                    if (result.getFinalValue() != null) {
                        if (functionValue.getReturnDataType() != null && !functionValue.getReturnDataType().isMatches(result.getFinalValue()))
                            throw new InvalidSyntaxException("Returned value's data type is different from specified (" + functionValue.getReturnDataType().getName() + ")");
                        if (functionValue.getReturnDataType() == null)
                            throw new InvalidSyntaxException("Found return statement but function must return nothing");
                    }
                    break;
                }
            }
            if ((result == null || result instanceof NullValue) && functionValue.getReturnDataType() != null) {
                throw new InvalidSyntaxException(hasReturnStatement ?
                        "Function specified return value's data type but return statement is empty" : "Missing return statement");
            }
            return result;
        }

        throw new InvalidCallException("Can't call " + function.getValue() + " because it's not a function");
    }

    protected RuntimeValue<?> evaluateClassCallExpression(ClassCallExpression classCallExpression, Environment lookupEnvironment, Environment argsEnvironment) {
        ArrayList<RuntimeValue<?>> args = new ArrayList<>();
        classCallExpression.getArgs().forEach(arg -> args.add(evaluate(arg, argsEnvironment).getFinalRuntimeValue()));

        RuntimeValue<?> rawClass = evaluate(classCallExpression.getCaller(), lookupEnvironment);

        if (rawClass instanceof ClassValue classValue) {
            ClassEnvironment classEnvironment;
            try {
                classEnvironment = Registries.CLASS_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class, String.class).newInstance(Registries.GLOBAL_ENVIRONMENT.getEntry().getValue(), classValue.getId());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            for (Statement statement : classValue.getBody()) {
                evaluate(statement, classEnvironment);
            }

            if (classEnvironment.hasConstructor()) {
                RuntimeValue<?> rawConstructor = classEnvironment.getConstructor(args);
                if (rawConstructor == null) throw new InvalidCallException("Class with id " + classValue.getId() + " doesn't have requested constructor");

                if (rawConstructor instanceof ConstructorValue constructorValue) {
                    if (constructorValue.getAccessModifiers().contains("private") && !argsEnvironment.hasParent(environment -> {
                        if (environment instanceof ClassEnvironment classEnvironment1) {
                            return classEnvironment1.getId().equals(classValue.getId());
                        }
                        return false;
                    })) {
                        throw new InvalidCallException("Requested constructor has private access");
                    }

                    VariableDeclarationEnvironment constructorEnvironment;
                    try {
                        constructorEnvironment = Registries.VARIABLE_DECLARATION_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class).newInstance(classEnvironment);
                    }
                    catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                    for (int i = 0; i < constructorValue.getArgs().size(); i++) {
                        CallArgExpression callArgExpression = constructorValue.getArgs().get(i);
                        constructorEnvironment.declareArgument(
                                callArgExpression.getId(),
                                callArgExpression.getDataType(),
                                args.get(i),
                                callArgExpression.isConstant(),
                                new HashSet<>());
                    }

                    for (Statement statement : constructorValue.getBody()) {
                        evaluate(statement, constructorEnvironment);
                    }
                }
            }

            return new ClassValue(classEnvironment, classValue.getBody());
        }
        if (rawClass instanceof DefaultClassValue defaultClassValue) {
            ClassEnvironment classEnvironment;
            try {
                classEnvironment = Registries.CLASS_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class, String.class).newInstance(Registries.GLOBAL_ENVIRONMENT.getEntry().getValue(), defaultClassValue.getId());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            defaultClassValue.getClassEnvironment().getVariables().forEach(variable -> {
                if (!variable.isArgument()) classEnvironment.declareVariable(variable.getId(), variable.getDataType(), variable.getValue(), variable.isConstant(), variable.getAccessModifiers());
                else classEnvironment.declareArgument(variable.getId(), variable.getDataType(), variable.getValue(), variable.isConstant(), variable.getAccessModifiers());
            });
            defaultClassValue.getClassEnvironment().getFunctions().forEach(function -> {
                if (function.getFunctionValue() instanceof DefaultFunctionValue defaultFunctionValue) {
                    defaultFunctionValue.setParentEnvironment(classEnvironment);
                    classEnvironment.declareFunction(function.getId(), defaultFunctionValue);
                }
            });
            defaultClassValue.getClassEnvironment().getConstructors().forEach(constructor -> {
                if (constructor instanceof DefaultConstructorValue defaultConstructorValue) {
                    defaultConstructorValue.setParentEnvironment(classEnvironment);
                    classEnvironment.declareConstructor(defaultConstructorValue);
                }
            });

            if (classEnvironment.hasConstructor()) {
                RuntimeValue<?> rawConstructor = classEnvironment.getConstructor(args);
                if (rawConstructor == null) throw new InvalidCallException("Class with id " + defaultClassValue.getId() + " doesn't have requested constructor");

                if (rawConstructor instanceof DefaultConstructorValue defaultConstructorValue) {
                    if (defaultConstructorValue.getAccessModifiers().contains("private") && !argsEnvironment.hasParent(environment1 -> {
                        if (environment1 instanceof ClassEnvironment classEnvironment1) {
                            return classEnvironment1.getId().equals(defaultClassValue.getId());
                        }
                        return false;
                    })) {
                        throw new InvalidCallException("Requested constructor has private access");
                    }

                    VariableDeclarationEnvironment constructorEnvironment;
                    try {
                        constructorEnvironment = Registries.VARIABLE_DECLARATION_ENVIRONMENT.getEntry().getValue().getConstructor(Environment.class).newInstance(classEnvironment);
                    }
                    catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                    defaultConstructorValue.run(args, constructorEnvironment);
                }
            }

            return new DefaultClassValue(classEnvironment);
        }

        throw new InvalidCallException("Can't call " + rawClass.getValue() + " because it's not a class");
    }

    protected RuntimeValue<?> evaluateMemberExpression(MemberExpression memberExpression, Environment environment) {
        RuntimeValue<?> object = evaluate(memberExpression.getObject(), environment);

        if (object instanceof VariableValue variableValue) {
            if (variableValue.getValue() instanceof ClassValue classValue) {
                return evaluate(memberExpression.getField(), classValue.getClassEnvironment(), environment);
            }
            if (variableValue.getValue() instanceof DefaultClassValue defaultClassValue) {
                return evaluate(memberExpression.getField(), defaultClassValue.getClassEnvironment(), environment);
            }
            throw new InvalidSyntaxException("Can't get members of " + object.getValue() + " because it's not a class");
        }
        if (object instanceof ClassValue classValue) {
            return evaluate(memberExpression.getField(), classValue.getClassEnvironment(), environment);
        }
        if (object instanceof DefaultClassValue defaultClassValue) {
            return evaluate(memberExpression.getField(), defaultClassValue.getClassEnvironment(), environment);
        }

        throw new InvalidSyntaxException("Can't get members of " + object.getValue() + " because it's not a class");
    }

    protected NumberValue<?> evaluateNumericBinaryExpression(NumberValue<?> leftValue, NumberValue<?> rightValue, String operator) {
        if (leftValue instanceof IntValue leftIntValue && rightValue instanceof IntValue rightIntValue) {
            int result;

            int left = leftIntValue.getValue();
            int right = rightIntValue.getValue();

            switch (operator) {
                case "+" -> result = left + right;
                case "-" -> result = left - right;
                case "*" -> result = left * right;
                case "/" -> result = left / right;
                case "%" -> result = left % right;
                case "^" -> result = (int) Math.pow(left, right);
                default -> throw new UnsupportedOperatorException(operator);
            }

            return new IntValue(result);
        }

        double result;

        double left = leftValue.getValue().doubleValue();
        double right = rightValue.getValue().doubleValue();

        switch (operator) {
            case "+" -> result = left + right;
            case "-" -> result = left - right;
            case "*" -> result = left * right;
            case "/" -> result = left / right;
            case "%" -> result = left % right;
            case "^" -> result = Math.pow(left, right);
            default -> throw new UnsupportedOperatorException(operator);
        }

        return new DoubleValue(result);
    }

    protected StringValue evaluateStringBinaryExpression(RuntimeValue<?> leftValue, RuntimeValue<?> rightValue, String operator) {
        StringBuilder result = new StringBuilder();

        switch (operator) {
            case "+" -> {
                String left = leftValue.getValue() == null ? "null" : leftValue.getValue().toString();
                String right = rightValue.getValue() == null ? "null" : rightValue.getValue().toString();
                result.append(left).append(right);
            }
            case "*" -> {
                String string;
                int number;

                if (leftValue instanceof StringValue stringValue && rightValue instanceof IntValue numberValue) {
                    string = stringValue.getValue();
                    number = numberValue.getValue();
                }
                else if (rightValue instanceof StringValue stringValue && leftValue instanceof IntValue numberValue) {
                    string = stringValue.getValue();
                    number = numberValue.getValue();
                }
                else throw new InvalidSyntaxException("Can only multiply string by a integer value");

                if (number < 0) throw new InvalidSyntaxException("Can't multiply string by a negative integer");

                result.repeat(string, number);
            }
            default -> throw new UnsupportedOperatorException(operator);
        }

        return new StringValue(result.toString());
    }



    protected RuntimeValue<?> evaluateIdentifier(Identifier identifier, Environment environment, Environment extra) {
        if (identifier instanceof VariableIdentifier) {
            RuntimeVariable runtimeVariable = environment.getVariableEnvironment(identifier.getId()).getVariable(identifier.getId());
            if (runtimeVariable != null) {
                if (runtimeVariable.getAccessModifiers().contains("private") &&
                        !environment.hasParent(environment.getVariableEnvironment(identifier.getId())))
                    throw new InvalidAccessException("Can't access variable " + identifier.getId() + " because it's private");
                if (!runtimeVariable.getAccessModifiers().contains("shared") && environment.isShared() && !runtimeVariable.isArgument())
                    throw new InvalidAccessException("Can't access variable " + identifier.getId() + " because it's not shared");

                return runtimeVariable.getValue();
            }

            throw new InvalidIdentifierException("Variable with identifier " + identifier.getId() + " doesn't exist");
        }

        if (identifier instanceof FunctionIdentifier functionIdentifier) {
            ArrayList<RuntimeValue<?>> args = new ArrayList<>();
            functionIdentifier.getArgs().forEach(arg -> args.add(evaluate(arg, extra).getFinalRuntimeValue()));
            RuntimeValue<?> runtimeFunction = environment.getFunctionEnvironment(identifier.getId(), args).getFunction(identifier.getId(), args);

            if (runtimeFunction != null) {
                if (runtimeFunction instanceof FunctionValue functionValue) {
                    if (functionValue.getAccessModifiers().contains("private") &&
                            !environment.hasParent(environment.getFunctionEnvironment(identifier.getId(), args)))
                        throw new InvalidAccessException("Can't access function " + identifier.getId() + " because it's private");
                    if (!functionValue.getAccessModifiers().contains("shared") && environment.isShared())
                        throw new InvalidAccessException("Can't access function " + identifier.getId() + " because it's not shared");
                }
                else if (runtimeFunction instanceof DefaultFunctionValue defaultFunctionValue) {
                    if (defaultFunctionValue.getAccessModifiers().contains("private") &&
                            !environment.hasParent(environment.getFunctionEnvironment(identifier.getId(), args)))
                        throw new InvalidAccessException("Can't access function " + identifier.getId() + " because it's private");
                    if (!defaultFunctionValue.getAccessModifiers().contains("shared") && environment.isShared())
                        throw new InvalidAccessException("Can't access function " + identifier.getId() + " because it's not shared");
                }

                return runtimeFunction;
            }

            throw new InvalidIdentifierException("Function with identifier " + identifier.getId() + " doesn't exist");
        }

        if (identifier instanceof ClassIdentifier) {
            RuntimeClass runtimeClass = Registries.GLOBAL_ENVIRONMENT.getEntry().getValue().getClass(identifier.getId());
            if (runtimeClass != null) return runtimeClass.getRuntimeValue();

            return evaluateIdentifier(new VariableIdentifier(identifier.getId()), environment,extra);
        }

        throw new InvalidIdentifierException("Invalid identifier " + identifier.getClass().getName());
    }
}
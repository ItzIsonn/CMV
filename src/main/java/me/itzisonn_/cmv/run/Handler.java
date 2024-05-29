package me.itzisonn_.cmv.run;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import lombok.Getter;
import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.ForLoop;
import me.itzisonn_.cmv.lang.main.IfCondition;
import me.itzisonn_.cmv.lang.main.Variable;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.main.WhileLoop;
import me.itzisonn_.cmv.lang.types.Type;
import me.itzisonn_.cmv.lang.types.VoidValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Handler {
    protected final ArrayList<Runnable> handles = new ArrayList<>();
    @Getter
    protected final ArrayList<Variable> variables = new ArrayList<>();
    protected ArrayList<String> body;
    @Getter
    protected int lineNumber;
    protected int bracketIndex = 0;
    protected CompletableHandler uncompletedHandler = null;
    protected String line;
    protected boolean isHandled = false;

    public Handler(ArrayList<String> body, int lineNumber) {
        this.body = body;
        this.lineNumber = lineNumber;

        handles.add(this::handleUncompleted);
        handles.add(this::handleFunction);
        handles.add(this::handleIf);
        handles.add(this::handleFor);
        handles.add(this::handleWhile);
        handles.add(this::handleVariable);
    }

    public void run() {
        for (String line : body) {
            lineNumber++;
            if (!line.isEmpty() && !line.startsWith("//")) {
                this.line = line;
                isHandled = false;
                for (Runnable runnable : handles) {
                    if (!isHandled) runnable.run();
                }
                if (!isHandled) throw new RuntimeException(lineNumber, "unknown statement");
            }
        }

        if (bracketIndex > 0) {
            throw new RuntimeException(lineNumber, "can't find close-bracket");
        }
    }

    protected void handleUncompleted() {
        isHandled = true;

        if (line.endsWith("{")) {
            bracketIndex++;
        }

        if (line.equals("}")) {
            bracketIndex--;

            if (bracketIndex == 0 && uncompletedHandler != null) {
                uncompletedHandler.complete();
                if (uncompletedHandler instanceof IfCondition || uncompletedHandler instanceof ForLoop || uncompletedHandler instanceof WhileLoop) {
                    uncompletedHandler.run();
                }
                uncompletedHandler = null;
                return;
            }
        }

        if (bracketIndex > 0 && uncompletedHandler != null) {
            uncompletedHandler.addLine(line);
            return;
        }

        isHandled = false;
    }

    protected void handleFunction() {
        isHandled = true;

        if (line.matches("([a-zA-Z_]+)\\s?\\((.*)\\)")) {
            String name;
            String stringParams;
            Matcher matcher = Pattern.compile("([a-zA-Z_]+)\\s?\\((.*)\\)").matcher(line);
            if (matcher.find()) {
                name = matcher.group(1).trim();
                stringParams = matcher.group(2).trim();
            }
            else throw new RuntimeException(lineNumber, "can't find function's name or params");

            ArrayList<Object> params = new ArrayList<>();
            if (!stringParams.isEmpty()) {
                for (String stringParam : Utils.split(stringParams, ',')) {
                    params.add(convertStatement(stringParam.trim()));
                }
            }

            Main.getGlobal().getFunction(name, params.size()).run(params);
            return;
        }

        isHandled = false;
    }

    protected void handleIf() {
        isHandled = true;

        if (line.startsWith("if")) {
            if (line.matches("if\\s?\\(.+\\)\\s?\\{")) {
                String expression;
                Matcher expressionMatcher = Pattern.compile("if\\s?\\((.+)\\)\\s?\\{").matcher(line);
                if (expressionMatcher.find()) expression = expressionMatcher.group(1);
                else throw new RuntimeException(lineNumber, "can't find if's expression");

                uncompletedHandler = new IfCondition(convertStatement(expression), this);
                return;
            }

            throw new RuntimeException(lineNumber, "incorrect introduce to the \"if\" statement");
        }

        isHandled = false;
    }

    protected void handleFor() {
        isHandled = true;

        if (line.startsWith("for")) {
            if (line.matches("for\\s?\\(.+\\)\\s?\\{")) {
                String stringArgs;
                Matcher argsMatcher = Pattern.compile("for\\s?\\((.+)\\)\\s?\\{").matcher(line);
                if (argsMatcher.find()) stringArgs = argsMatcher.group(1);
                else throw new RuntimeException(lineNumber, "can't find for's args");

                ArrayList<String> args = new ArrayList<>();
                for (String stringParam : Utils.split(stringArgs, ';')) {
                    args.add(stringParam.trim());
                }
                if (args.size() != 3)
                    throw new RuntimeException(lineNumber, "expected 3 arguments but found " + args.size());

                uncompletedHandler = new ForLoop(args.get(0), args.get(1), args.get(2), this);
                return;
            }

            throw new RuntimeException(lineNumber, "incorrect introduce to the \"for\" statement");
        }

        isHandled = false;
    }

    protected void handleWhile() {
        isHandled = true;

        if (line.startsWith("while")) {
            if (line.matches("while\\s?\\(.+\\)\\s?\\{")) {
                String expression;
                Matcher expressionMatcher = Pattern.compile("while\\s?\\((.+)\\)\\s?\\{").matcher(line);
                if (expressionMatcher.find()) expression = expressionMatcher.group(1);
                else throw new RuntimeException(lineNumber, "can't find while's expression");

                uncompletedHandler = new WhileLoop(expression, this);
                return;
            }

            throw new RuntimeException(lineNumber, "incorrect introduce to the \"while\" statement");
        }

        isHandled = false;
    }

    protected void handleVariable() {
        isHandled = true;

        if (line.startsWith("var") || line.startsWith("val")) {
            if (!line.matches("(var|val) ([a-zA-Z_]+)(\\s?:\\s?([a-zA-Z]+))?\\s?(=\\s?(.+))?"))
                throw new RuntimeException(lineNumber, "incorrect introduce to the variable");

            boolean isConst;
            String name;
            Type type;
            String value;
            Matcher matcher = Pattern.compile("(var|val) ([a-zA-Z_]+)(\\s?:\\s?([a-zA-Z]+))?\\s?(=\\s?(.+))?").matcher(line);

            if (matcher.find()) {
                isConst = matcher.group(1).trim().equals("val");
                name = matcher.group(2).trim();
                type = matcher.group(4) == null ? Type.ANY : Type.ofString(matcher.group(4).trim());
                value = matcher.group(6);
            }
            else throw new RuntimeException(lineNumber, "incorrect introduce to the variable");

            if (getVariable(name) != null)
                throw new RuntimeException(lineNumber, "variable \"" + name + "\" already exists");

            if (value == null && isConst) throw new RuntimeException(lineNumber, "constant variable must be initialized");

            Variable variable = new Variable(name, type, value == null ? new VoidValue() : convertStatement(value.trim()), isConst);
            variables.add(variable);
            return;
        }

        if (line.matches("([a-zA-Z_]+)\\s?([+-/*=])?=\\s?(.+)")) {
            String name;
            String operator;
            String value;
            Matcher matcher = Pattern.compile("([a-zA-Z_]+)\\s?([+-/*=])?=\\s?(.+)").matcher(line);

            if (matcher.find()) {
                name = matcher.group(1).trim();
                operator = matcher.group(2);
                value = matcher.group(3).trim();
            }
            else throw new RuntimeException(lineNumber, "can't find operator or variable's name or value");

            if (operator != null) getVariable(name).setValue(convertStatement(name + " " + operator.trim() + " " + value));
            else getVariable(name).setValue(convertStatement(value));
            return;
        }

        if (line.matches("([a-zA-Z_]+)\\s?([+-])\\2")) {
            String name;
            String operator;
            Matcher matcher = Pattern.compile("([a-zA-Z_]+)\\s?([+-])\\2").matcher(line);

            if (matcher.find()) {
                name = matcher.group(1).trim();
                operator = matcher.group(2).trim();
            }
            else throw new RuntimeException(lineNumber, "can't find variable's name");

            getVariable(name).setValue(convertStatement(name + " " + operator + " 1"));
            return;
        }

        isHandled = false;
    }



    protected Variable getVariable(String name) {
        for (Variable variable : getVariables()) {
            if (variable.getName().equals(name)) {
                return variable;
            }
        }

        return null;
    }

    protected Object convertStatement(String statement) {
        try {
            HashMap<String, Object> variablesMap = new HashMap<>();
            Expression expression = new Expression(statement, Main.getGlobal().getFunctionsConfiguration());
            for (Variable variable : getVariables()) {
                if (variable.getValue() instanceof VoidValue) {
                    if (expression.getUsedVariables().contains(variable.getName()))
                        throw new RuntimeException(lineNumber, "value of variable \"" + variable.getName() + "\" isn't defined");
                }
                else variablesMap.put(variable.getName(), variable.getValue());
            }

            expression.withValues(variablesMap);
            return Utils.convertBigDecimal(expression.evaluate().getValue());
        }
        catch (EvaluationException | ParseException ignore) {
            throw new RuntimeException(lineNumber, "unknown or incorrect statement: " + statement);
        }
    }
}
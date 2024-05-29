package me.itzisonn_.cmv.run;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.Token;
import lombok.Getter;
import me.itzisonn_.cmv.lang.main.functions.AbstractEvalFunction;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.Function;
import me.itzisonn_.cmv.lang.main.FunctionVariable;
import me.itzisonn_.cmv.lang.main.functions.input.GetArgFunction;
import me.itzisonn_.cmv.lang.main.functions.input.GetLineFunction;
import me.itzisonn_.cmv.lang.main.functions.print.PrintFunction;
import me.itzisonn_.cmv.lang.main.functions.print.PrintlnFunction;
import me.itzisonn_.cmv.lang.main.functions.strings.GetCharAtFunction;
import me.itzisonn_.cmv.lang.main.functions.strings.GetLengthFunction;
import me.itzisonn_.cmv.lang.main.functions.strings.SetCharAtFunction;
import me.itzisonn_.cmv.lang.types.Type;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Global extends Handler {
    private final ArrayList<Function> functions = new ArrayList<>();

    public Global(ArrayList<String> body) {
        super(body, 0);

        functions.add(new PrintFunction());
        functions.add(new PrintlnFunction());
        functions.add(new GetArgFunction());
        functions.add(new GetLineFunction());
        functions.add(new GetCharAtFunction());
        functions.add(new SetCharAtFunction());
        functions.add(new GetLengthFunction());

        handles.add(1, this::introduceFunction);
    }

    private void introduceFunction() {
        isHandled = true;

        if (line.startsWith("function")) {
            if (line.matches("function ([a-zA-Z_]+)\\s?\\((.*)\\)\\s?(:\\s?([a-zA-Z]+)\\s?)?\\{")) {
                String name;
                String stringParams;
                Type type;
                Matcher matcher = Pattern.compile("function ([a-zA-Z_]+)\\s?\\((.*)\\)\\s?(:\\s?([a-zA-Z]+)\\s?)?\\{").matcher(line);
                if (matcher.find()) {
                    name = matcher.group(1).trim();
                    stringParams = matcher.group(2).trim();
                    type = matcher.group(4) == null ? null : Type.ofString(matcher.group(4).trim());
                }
                else throw new RuntimeException(lineNumber, "can't find function's name, params or return type");

                ArrayList<FunctionVariable> params = new ArrayList<>();
                if (!stringParams.isEmpty()) {
                    for (String param : Utils.split(stringParams, ',')) {
                        param = param.trim();

                        if (!param.matches("(var|val) ([a-zA-Z_]+)(\\s?:\\s?([a-zA-Z]+))?"))
                            throw new RuntimeException(lineNumber, "incorrect introduce to the variable");

                        boolean isConst;
                        String variableName;
                        Type variableType;
                        Matcher variableMatcher = Pattern.compile("(var|val) ([a-zA-Z_]+)(\\s?:\\s?([a-zA-Z]+))?").matcher(param);

                        if (variableMatcher.find()) {
                            isConst = variableMatcher.group(1).trim().equals("val");
                            variableName = variableMatcher.group(2).trim();
                            variableType = variableMatcher.group(4) == null ? Type.ANY : Type.ofString(variableMatcher.group(4).trim());
                        }
                        else throw new RuntimeException(lineNumber, "incorrect introduce to the variable");

                        if (containsVariable(params, variableName))
                            throw new RuntimeException(lineNumber, "function variable \"" + variableName + "\" already exists");

                        FunctionVariable variable = new FunctionVariable(variableName, variableType, isConst);
                        params.add(variable);
                    }
                }

                if (existFunction(name, params.size())) {
                    throw new RuntimeException(lineNumber, "function \"" + name + "\" with " + params.size() + " param(s) already exists");
                }

                Function function = new Function(name, params, type, this);
                functions.add(function);

                uncompletedHandler = function;
                return;
            }

            throw new RuntimeException(lineNumber, "incorrect introduce to the function");
        }

        isHandled = false;
    }

    public Function getFunction(String name, int size) {
        for (Function function : functions) {
            if (name.equals(function.getName()) && size == function.getParams().size()) {
                return function;
            }
        }

        for (Function function : functions) {
            if (name.equals(function.getName())) {
                return function;
            }
        }

        throw new RuntimeException(lineNumber, "can't find function with name \"" + name + "\"");
    }

    private boolean existFunction(String name, int size) {
        for (Function function : functions) {
            if (name.equals(function.getName()) && size == function.getParams().size()) {
                return true;
            }
        }

        return false;
    }

    private boolean containsVariable(ArrayList<FunctionVariable> variables, String name) {
        for (FunctionVariable variable : variables) {
            if (variable.getName().equals(name)) return true;
        }

        return false;
    }

    public ExpressionConfiguration getFunctionsConfiguration() {
        ExpressionConfiguration configuration = ExpressionConfiguration.defaultConfiguration();

        for (Function function : functions) {
            configuration.getFunctionDictionary().addFunction(function.getName(),
                    new AbstractEvalFunction(function.getParams()) {
                        @Override
                        public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) {
                            ArrayList<Object> params = new ArrayList<>();
                            for (EvaluationValue value : evaluationValues) {
                                params.add(Utils.convertBigDecimal(value.getValue()));
                            }
                            return new EvaluationValue(function.runWithReturn(params), ExpressionConfiguration.defaultConfiguration());
                        }
                    });
        }

        return configuration;
    }
}
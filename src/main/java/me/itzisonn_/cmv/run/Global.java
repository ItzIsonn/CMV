package me.itzisonn_.cmv.run;

import lombok.Getter;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.main.Function;
import me.itzisonn_.cmv.lang.main.functions.input.GetArgFunction;
import me.itzisonn_.cmv.lang.main.functions.input.GetLineFunction;
import me.itzisonn_.cmv.lang.main.functions.print.PrintFunction;
import me.itzisonn_.cmv.lang.main.functions.print.PrintlnFunction;
import me.itzisonn_.cmv.lang.main.functions.strings.GetCharAtFunction;
import me.itzisonn_.cmv.lang.main.functions.strings.GetLengthFunction;
import me.itzisonn_.cmv.lang.main.functions.strings.SetCharAtFunction;

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
            if (line.matches("function [a-zA-Z_]*\\s?\\(\\s?\\)\\s?\\{")) {
                String name;
                Matcher nameMatcher = Pattern.compile("function ([a-zA-Z_]*)\\s?\\(\\s?\\)\\s?\\{").matcher(line);
                if (nameMatcher.find()) name = nameMatcher.group(1);
                else throw new RuntimeException(lineNumber, "can't find function's name");

                Function function = new Function(name, new ArrayList<>(), this);
                functions.add(function);

                uncompletedHandler = function;
                return;
            }

            if (line.matches("function [a-zA-Z_]*\\s?\\(.+\\)\\s?\\{")) {
                String name;
                String stringParams;
                Matcher matcher = Pattern.compile("function ([a-zA-Z_]*)\\s?\\((.*)\\)\\s?\\{").matcher(line);
                if (matcher.find()) {
                    name = matcher.group(1).trim();
                    stringParams = matcher.group(2).trim();
                }
                else throw new RuntimeException(lineNumber, "can't find function's name or params");

                ArrayList<String> params = new ArrayList<>();
                for (String param : new ArrayList<>(Arrays.asList(stringParams.split(",")))) {
                    params.add(param.replaceAll("var", "").trim());
                }
                Function function = new Function(name, params, this);
                functions.add(function);

                uncompletedHandler = function;
                return;
            }

            throw new RuntimeException(lineNumber, "incorrect introduce to the function");
        }

        isHandled = false;
    }

    public Function getFunction(String name) {
        for (Function function : functions) {
            if (name.equals(function.getName())) {
                return function;
            }
        }

        throw new RuntimeException(lineNumber, "can't find function with name \"" + name + "\"");
    }

//    public ExpressionConfiguration getFunctionsConfiguration() {
//        ExpressionConfiguration configuration = ExpressionConfiguration.defaultConfiguration();
//
//        for (Function function : functions) {
//            configuration.getFunctionDictionary().addFunction(function.getName(),
//                    new AbstractFunction() {
//                        @Override
//                        public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) {
//                            for (String name : function.getParamsNames()) {
//                                getFunctionParameterDefinitions().add(
//                                        FunctionParameterDefinition.builder().name(name).isVarArg(false).isLazy(false).nonZero(false).nonNegative(false).build());
//                            }
//
//                            ArrayList<String> params = new ArrayList<>();
//                            for (EvaluationValue value : evaluationValues) {
//                                params.add(value.getStringValue());
//                            }
//                            return new EvaluationValue(function.runWithReturn(params), ExpressionConfiguration.defaultConfiguration());
//                        }
//                    });
//            ;
//        }
//
//        return configuration;
//    }
}
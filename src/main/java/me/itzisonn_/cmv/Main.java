package me.itzisonn_.cmv;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.parser.ParseException;
import com.ezylang.evalex.parser.Token;
import lombok.Getter;
import me.itzisonn_.cmv.run.Global;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    @Getter
    private static Global global;

    public static void main(String[] args) throws EvaluationException, ParseException {
//        Expression expression = new Expression("double(5)",
//                ExpressionConfiguration.defaultConfiguration().withAdditionalFunctions(
//                        Map.entry("double", new AbstractFunctionX(new ArrayList<>(List.of("num"))) {
//                            @Override
//                            public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) {
//                                return new EvaluationValue(Integer.parseInt(evaluationValues[0].getStringValue()) * 2, ExpressionConfiguration.defaultConfiguration());
//                            }
//                        })
//                ));
//        System.out.println(expression.evaluate().getStringValue());
//
//        if (1==1) return;

        if (args.length == 0) {
            System.out.println("  Usage: <file_to_run>");
            return;
        }

        if (args.length >= 2) {
            System.out.println("  Usage: <file_to_run>");
            return;
        }

        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line = reader.readLine();

            while (line != null) {
                lines.add(Utils.removeDuplicatedSpaces(line.trim()));
                line = reader.readLine();
            }

            reader.close();
        }
        catch (IOException e) {
            System.err.println("  Can't find file with name \"" + args[0] + "\"");
            return;
        }

        System.out.println("  CMV-v1.1.0: running file with name \"" + args[0] + "\"");
        global = new Global(lines);
        global.run();
    }
}
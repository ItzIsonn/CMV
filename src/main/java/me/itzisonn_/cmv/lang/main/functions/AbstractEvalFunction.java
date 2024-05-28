package me.itzisonn_.cmv.lang.main.functions;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.parser.Token;
import me.itzisonn_.cmv.lang.main.FunctionVariable;

import java.util.ArrayList;

public class AbstractEvalFunction extends AbstractFunction {
    public AbstractEvalFunction(ArrayList<FunctionVariable> params) {
        for (FunctionVariable variable : params) {
            getFunctionParameterDefinitions().add(FunctionParameterDefinition.builder().name(variable.getName())
                    .isVarArg(false).isLazy(false).nonZero(false).nonNegative(false).build());
        }
    }

    @Override
    public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) {
        return null;
    }
}

package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Function;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.value.Value;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Chandler on 4/12/17.
 */
public class FunctionCall implements Value, Statement {

    Function ref;
    List<Value> parameters;

    public FunctionCall(CMinusParser.FunctionCallContext ctx, Scope scope) {
        String name = ctx.ID().getText();
        Optional<Function> func = scope.getFunction(name);

        if (func.isPresent()) {
            ref = func.get();
        } else {
            throw new RuntimeException("Function " + name + " was not found.");
        }

        parameters = ctx.value().stream().map(p -> Value.parse(p, scope)).collect(Collectors.toList());

        if (parameters.size() != ref.parameters.size()) {
            throw new RuntimeException("Function " + name + " does not have " + parameters.size() + " parameters.");
        }

        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i).type() != ref.parameters.get(i).type()) {
                throw new RuntimeException("Function " + name + " does not have "
                        + parameters.get(i).type().code() + " as a parameter");
            }
        }
    }

    @Override
    public Type type() {
        return ref.type();
    }

    @Override
    public String code() {
        StringBuilder s = new StringBuilder(ref.name)
                .append('(');

        for (Value value: parameters) {
            s.append(value.value()).append(", ");
        }

        if (!parameters.isEmpty()) {
            s.delete(s.length() - 2, s.length());
        }

        s.append(')');

        return s.toString();
    }

    @Override
    public String value() {
        return code();
    }
}

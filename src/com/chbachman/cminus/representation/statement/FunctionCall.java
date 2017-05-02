package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Struct;
import com.chbachman.cminus.representation.function.Function;
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
        this(ctx, scope, makeFunction(ctx, scope));
    }

    protected FunctionCall(CMinusParser.FunctionCallContext ctx, Scope scope, Function ref) {
        this.ref = ref;
        String name = ctx.ID().getText();
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

    private static Function makeFunction(CMinusParser.FunctionCallContext ctx, Scope scope) {
        String name = ctx.ID().getText();
        List<Value> parameters = ctx.value().stream().map(p -> Value.parse(p, scope)).collect(Collectors.toList());
        Optional<Function> func = scope.getFunction(name, parameters);
        Function ref;

        if (func.isPresent()) {
            ref = func.get();
        } else {
            throw new RuntimeException("Function " + name + " was not found.");
        }

        return ref;
    }

    @Override
    public Type type() {
        return ref.type();
    }

    @Override
    public String code() {
        return value() + ';';
    }

    @Override
    public String value() {
        StringBuilder s = new StringBuilder(ref.getCName())
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
}

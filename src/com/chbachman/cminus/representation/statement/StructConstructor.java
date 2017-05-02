package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Struct;
import com.chbachman.cminus.representation.function.Function;
import com.chbachman.cminus.representation.value.Value;
import com.chbachman.cminus.representation.value.Variable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Chandler on 4/23/17.
 */
public class StructConstructor extends FunctionCall {
    public final Struct struct;

    public StructConstructor(CMinusParser.FunctionCallContext ctx, Scope scope) {
        super(ctx, scope, makeFunction(ctx, scope));

        // Since we already checked all this in the 'makeFunction' call, we can just directly access it.
        this.struct = scope.getStruct(ctx.ID().getText()).get();
    }

    private static Function makeFunction(CMinusParser.FunctionCallContext ctx, Scope scope) {
        // TODO: Fix this
        String name = ctx.ID().getText();
        Optional<Struct> struct = scope.getStruct(name);
        List<Value> parameters = ctx.value().stream().map(v -> Value.parse(v, scope)).collect(Collectors.toList());
        Function ref;

        if (struct.isPresent()) {
            ref = struct.get().getInit(parameters).get();
        } else {
            throw new RuntimeException("Function " + name + " was not found.");
        }

        return ref;
    }
}

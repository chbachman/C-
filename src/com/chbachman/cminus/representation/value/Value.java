package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Typed;
import com.chbachman.cminus.representation.statement.FunctionCall;

import java.util.Optional;

/**
 * Created by Chandler on 4/12/17.
 */
public interface Value extends Typed {

    static Value parse(CMinusParser.ValueContext ctx, Scope scope) {
        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            Optional<Variable> v = scope.getVariable(name);

            if (v.isPresent()) {
                return v.get();
            } else {
                throw new RuntimeException("Invalid Variable: " + ctx.ID());
            }
        }

        if (ctx.literal() != null) {
            return new Literal(ctx.literal());
        }

        if (ctx.functionCall() != null) {
            return new FunctionCall(ctx.functionCall(), scope);
        }

        throw new RuntimeException("Type of value isn't implemented. " + ctx.getText());
    }

    String value();

}

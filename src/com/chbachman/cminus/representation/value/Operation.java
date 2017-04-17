package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;

/**
 * Created by Chandler on 4/14/17.
 */
public class Operation implements Value {

    public final String operation;
    public final Value left;
    public final Value right;

    public Operation(CMinusParser.ValueContext ctx, Scope scope) {
        switch (ctx.op.getText()) {
            case "^": operation = "^"; break;
            case "*": operation = "*"; break;
            case "/": operation = "/"; break;
            case "+": operation = "+"; break;
            case "-": operation = "-"; break;
            case "%": operation = "%"; break;
            case "==": operation = "=="; break;
            default: throw new RuntimeException("Operation " + ctx.getText() + " is not implemented.");
        }

        left = Value.parse(ctx.value(0), scope);
        right = Value.parse(ctx.value(1), scope);

        if (left.type() != right.type()) {
            throw new RuntimeException("Types of " + ctx.getText() + " are not the same.");
        }

    }

    @Override
    public Type type() {
        return left.type();
    }

    @Override
    public String value() {
        return left.value() + " " + operation + " " + right.value();
    }
}

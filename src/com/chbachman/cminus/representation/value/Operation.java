package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Parser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Chandler on 4/14/17.
 * Handles basic operations such as * / - $ and ==
 * TODO: Allow for custom operator overloading
 */
public class Operation implements Value {

    private final String operation;
    private final Value left;
    private final Value right;

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

        left = Parser.parse(ctx.value(0), scope);
        right = Parser.parse(ctx.value(1), scope);

        if (left.type() != right.type()) {
            throw new RuntimeException("Types of " + ctx.getText() + " are not the same.");
        }

    }

    @Override
    public Type type() {
        return left.type();
    }

    @Override
    @NotNull
    public String value() {
        return left.value() + " " + operation + " " + right.value();
    }
}

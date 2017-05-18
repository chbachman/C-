package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Parser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Chandler on 4/14/17.
 * Handles Parenthesis around values.
 */
public class Paren implements Value {
    private Value value;

    public Paren(CMinusParser.ValueContext ctx, Scope scope) {
        this.value = Parser.parse(ctx.value(0), scope);
    }

    @Override
    public Type type() {
        return value.type();
    }

    @Override
    @NotNull
    public String value() {
        return '(' + value.value() + ')';
    }
}

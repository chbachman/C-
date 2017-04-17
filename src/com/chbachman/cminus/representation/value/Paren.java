package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;

/**
 * Created by Chandler on 4/14/17.
 */
public class Paren implements Value {
    Value value;

    public Paren(CMinusParser.ValueContext ctx, Scope scope) {
        this.value = Value.parse(ctx.value(0), scope);
    }

    @Override
    public Type type() {
        return value.type();
    }

    @Override
    public String value() {
        return '(' + value.value() + ')';
    }
}

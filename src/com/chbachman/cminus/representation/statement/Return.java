package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Parser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.value.Value;

/**
 * Created by Chandler on 4/12/17.
 * Handles return values for a function.
 */
public class Return implements Statement {

    private Value value;

    public Return(CMinusParser.RetContext ctx, Scope scope) {
        this.value = Parser.parse(ctx.value(), scope);
    }

    public Return(Value value) {
        this.value = value;
    }

    @Override
    public String code() {
        return "return " + value.value() + ";";
    }
}

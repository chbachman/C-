package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.value.Value;

/**
 * Created by Chandler on 4/16/17.
 */
public class Assignment implements Value, Statement {

    final String name;
    final Value value;

    public Assignment(CMinusParser.AssignmentContext ctx, Scope scope) {
        this.name = ctx.ID().getText();
        this.value = Value.parse(ctx.value(), scope);
    }

    public Assignment(String name, Value value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Type type() {
        return value.type();
    }

    @Override
    public String code() {
        return name + " = " + value.value() + ";";
    }

    @Override
    public String value() {
        return name + " = " + value.value();
    }
}

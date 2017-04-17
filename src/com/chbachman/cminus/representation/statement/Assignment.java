package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.value.Value;

/**
 * Created by Chandler on 4/16/17.
 */
public class Assignment implements Value, Statement {

    public Assignment(CMinusParser.AssignmentContext ctx, Scope scope) {

    }

    @Override
    public Type type() {
        return null;
    }

    @Override
    public String code() {
        return null;
    }

    @Override
    public String value() {
        return null;
    }
}

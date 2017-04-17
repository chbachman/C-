package com.chbachman.cminus.representation.control;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Value;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chandler on 4/14/17.
 */
public class IfStatement implements Control {

    Value value;

    public IfStatement(CMinusParser.IfStatementContext ctx, Scope scope) {
        this.value = Value.parse(ctx.value(0), scope);
    }

    @Override
    public String first() {
        StringBuilder s = new StringBuilder("if (")
                .append(value.value())
                .append(") {");
        return s.toString();
    }

    @Override
    public String last() {
        return "}";
    }
}

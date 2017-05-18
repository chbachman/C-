package com.chbachman.cminus.representation.control;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Parser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.function.CodeBlockHolder;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Value;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chandler on 4/14/17.
 */
public class IfStatement extends CodeBlockHolder implements Control {

    Value value;

    public IfStatement(CMinusParser.IfStatementContext ctx, Scope scope) {
        super(ctx.codeBlock().get(0), scope);
        this.value = Parser.parse(ctx.value(0), scope);
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

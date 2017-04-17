package com.chbachman.cminus.representation.control;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Value;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chandler on 4/14/17.
 */
public class ForStatement implements Control {

    Value minimum;
    Value maximum;

    Variable index;

    public ForStatement(CMinusParser.ForStatementContext ctx, Scope scope) {
        if (ctx.range() != null) {
            CMinusParser.RangeContext range = ctx.range();

            minimum = Value.parse(range.value(0), scope);
            maximum = Value.parse(range.value(1), scope);

            if (minimum.type() != Type.Native.INT.type || maximum.type() != Type.Native.INT.type) {
                throw new RuntimeException("For Loop without int range. " + range.getText());
            }

            index = new Variable(range.ID().getText(), Type.Native.INT.type);
        }
    }

    @Override
    public void setupScope(Scope scope) {
        scope.addVariable(index);
    }

    @Override
    public String first() {
        StringBuilder s = new StringBuilder("for (")
                .append("int ")
                .append(index.name)
                .append(" = ")
                .append(minimum.value())
                .append("; ")
                .append(index.name)
                .append(" <= ")
                .append(maximum.value())
                .append("; ")
                .append(index.name)
                .append("++) {");

        return s.toString();
    }

    @Override
    public String last() {
        return "}";
    }
}

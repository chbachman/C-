package com.chbachman.cminus.representation.control;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.function.CodeBlock;
import com.chbachman.cminus.representation.Scope;

/**
 * Created by Chandler on 4/14/17.
 */
public interface Control extends CodeBlock {

    static Control parse(CMinusParser.ControlContext ctx, Scope scope) {
        if (ctx.ifStatement() != null) {
            return new IfStatement(ctx.ifStatement(), scope);
        }

        if (ctx.forStatement() != null) {
            return new ForStatement(ctx.forStatement(), scope);
        }

        throw new RuntimeException("Control Statement: " + ctx.getText() + " has not been implemented yet.");
    }

}

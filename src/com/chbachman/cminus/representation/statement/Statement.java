package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.value.Variable;

/**
 * Created by Chandler on 4/12/17.
 */
public interface Statement {

    static Statement parse(CMinusParser.StatementContext ctx, Scope scope) {

        if (ctx.print() != null) {
            return new Print(ctx.print(), scope);
        }

        if (ctx.variable() != null) {
            return new Variable(ctx.variable(), scope);
        }

        if (ctx.ret() != null) {
            return new Return(ctx.ret(), scope);
        }

        if (ctx.assignment() != null) {
            return new Assignment(ctx.assignment(), scope);
        }

        throw new RuntimeException("The statement type: " + ctx.getText() + " is not implemented yet.");
    }

    String code();


}

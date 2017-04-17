package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.value.Variable;

/**
 * Created by Chandler on 4/12/17.
 */
public interface Statement {

    static Statement parse(CMinusParser.StatementContext cxt, Scope scope) {

        if (cxt.print() != null) {
            return new Print(cxt.print(), scope);
        }

        if (cxt.variable() != null) {
            return new Variable(cxt.variable(), scope);
        }

        if (cxt.ret() != null) {
            return new Return(cxt.ret(), scope);
        }

        if (cxt.assignment() != null) {

        }

        throw new RuntimeException("The statement type: " + cxt.getText() + " is not implemented yet.");
    }

    String code();


}

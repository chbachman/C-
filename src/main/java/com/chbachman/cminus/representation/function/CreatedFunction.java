package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;

/**
 * Created by Chandler on 4/16/17.
 */
public class CreatedFunction extends Function {

    public CreatedFunction(Type type, String name) {
        super(type, name, new ArrayList<>());
    }

    public CreatedFunction addStatement(Statement s) {
        this.statements.add(s);
        return this;
    }
}

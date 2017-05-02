package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chandler on 4/16/17.
 */
public class CreatedFunction extends Function {

    public CreatedFunction(Type type, String name) {
        super(type, name, new ArrayList<>());
    }

    public CreatedFunction addParameter(Variable p) {
        this.parameters.add(p);
        return this;
    }

    public CreatedFunction addStatement(Statement s) {
        this.statements.add(s);
        return this;
    }
}

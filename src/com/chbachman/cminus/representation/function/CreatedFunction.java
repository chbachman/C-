package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chandler on 4/16/17.
 */
public class CreatedFunction extends Function {

    List<Statement> statements;

    public CreatedFunction(Type type, String name) {
        super(type, name);
        statements = new ArrayList<>();
    }

    public CreatedFunction addParameter(Variable p) {
        this.parameters.add(p);
        return this;
    }

    public CreatedFunction addStatement(Statement s) {
        this.statements.add(s);
        return this;
    }

    @Override
    public String first() {
        StringBuilder s = new StringBuilder(super.first());

        for (Statement st : statements) {

        }

        return s.toString();
    }

    @Override
    public String last() {
        return super.last();
    }
}

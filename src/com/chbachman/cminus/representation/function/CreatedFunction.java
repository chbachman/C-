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

    final List<Statement> statements;

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

    public String middle() {
        StringBuilder s = new StringBuilder();
        for (Statement st : statements) {
            s.append(st.code() + '\n');
        }
        return s.toString();
    }
}

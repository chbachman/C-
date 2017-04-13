package com.chbachman.cminus.representation;

import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chandler on 4/13/17.
 */
public class Main implements CodeBlock{

    List<Statement> statements = new ArrayList<>();

    public Main() {

    }

    @Override
    public void addStatement(Statement s) {
        statements.add(s);
    }

    @Override
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public List<Variable> getVariablesToInit() {
        return Collections.EMPTY_LIST;
    }
}

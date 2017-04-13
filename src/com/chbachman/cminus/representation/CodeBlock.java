package com.chbachman.cminus.representation;

import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Variable;

import java.util.List;

/**
 * Created by Chandler on 4/13/17.
 */
public interface CodeBlock {

    void addStatement(Statement s);

    List<Statement> getStatements();

    List<Variable> getVariablesToInit();
}

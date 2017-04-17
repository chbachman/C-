package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Variable;

import java.util.List;

/**
 * Created by Chandler on 4/13/17.
 */
public interface CodeBlock {
    String first();

    String last();

    default void setupScope(Scope scope) {}
}

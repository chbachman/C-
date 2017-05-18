package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Parser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.value.Value;
import com.chbachman.cminus.representation.value.Variable;

import java.util.Optional;

/**
 * Created by Chandler on 4/16/17.
 */
public class Assignment implements Value, Statement {

    final String name;
    final Value value;

    public Assignment(CMinusParser.AssignmentContext ctx, Scope scope) {
        String name = ctx.ID().getText();
        this.value = Parser.parse(ctx.value(), scope);

        Variable var = scope.getVariable(name);

        if (var != null) {
            Variable v = var;

            v.value = Optional.of(value);
            this.name = v.name;
        } else {
            throw new RuntimeException("Could not find variable: " + name);
        }
    }

    public Assignment(String name, Value value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Type type() {
        return value.type();
    }

    @Override
    public String code() {
        return name + " = " + value.value() + ";";
    }

    @Override
    public String value() {
        return name + " = " + value.value();
    }
}

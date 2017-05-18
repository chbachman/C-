package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.Start;
import com.chbachman.cminus.representation.Parser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.statement.Statement;
import org.antlr.v4.runtime.RecognitionException;

import java.util.Optional;

/**
 * Created by Chandler on 4/12/17.
 */
public class Variable implements Value, Statement {

    public final String name;
    public Optional<Value> value;
    public final Type type;
    public final boolean newVariable;

    public Variable(CMinusParser.VariableContext ctx, Scope scope) {
        this.name = ctx.ID().getText();

        newVariable = ctx.var != null;

        if (ctx.value() != null) {
            this.value = Optional.of(Parser.parse(ctx.value(), scope));
            this.type = this.value.get().type();
        } else {
            this.type = Type.Companion.from(ctx.type());
            this.value = Optional.empty();
        }
    }

    public Variable(CMinusParser.ParameterContext ctx) {
        this.name = ctx.ID().getText();

        newVariable = true;

        this.type = Type.Companion.from(ctx.type());
        this.value = Optional.empty();
    }

    public Variable(String name, Value v) {
        this.name = name;
        this.value = Optional.of(v);
        this.type = v.type();
        this.newVariable = true;
    }

    public Variable(String name, Type t) {
        this.name = name;
        this.value = Optional.empty();
        this.type = t.type();
        this.newVariable = true;
    }

    public Variable(Variable parent, Variable child) {
        this.name = parent.name + "." + child.name;
        this.value = child.value;
        this.type = child.type;
        this.newVariable = true;
    }

    @Override
    public String code() {
        if (value.isPresent()) {
            String code = "";
            // Add the Type to make it a new variable
            if (newVariable) {
                code = value.get().type().code() + " ";
            }

            return code + name + " = " + value.get().value() + ";";
        } else {
            return type.code() + " " + name + ";";
        }

    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public String value() {
        return name;
    }
}

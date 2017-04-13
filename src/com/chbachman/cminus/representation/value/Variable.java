package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.statement.Statement;

import java.util.Optional;

/**
 * Created by Chandler on 4/12/17.
 */
public class Variable implements Value, Statement {

    public final String name;
    public final Optional<Value> value;
    public final Optional<Type> type;
    public final boolean newVariable;

    public Variable(CMinusParser.VariableContext ctx, Scope scope) {
        this.name = ctx.ID().getText();

        newVariable = ctx.VARIABLE_DECLARATION() != null;

        if (ctx.value() != null) {
            this.value = Optional.of(Value.parse(ctx.value(), scope));
            this.type = Optional.empty();
        } else {
            this.type = Optional.of(Type.from(ctx.type()));
            this.value = Optional.empty();
        }


        scope.addVariable(this);
    }

    @Override
    public String code() {
        if (value.isPresent()) {
            String code;
            if (newVariable) {
                code = "";
            } else {
                code = value.get().type().code() + " ";
            }

            return code + name + " = " + value.get().value() + ";";
        } else if (type.isPresent()) {
            return type.get().code() + " " + name + ";";
        } else {
            throw new RuntimeException("Variable doesn't have a Type or a Value");
        }

    }

    @Override
    public Type type() {
        return value.isPresent() ? value.get().type() : type.get();
    }

    @Override
    public String value() {
        return name;
    }
}

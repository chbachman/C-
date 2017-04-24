package com.chbachman.cminus.representation;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.function.CodeBlock;
import com.chbachman.cminus.representation.function.CreatedFunction;
import com.chbachman.cminus.representation.statement.Assignment;
import com.chbachman.cminus.representation.statement.Return;
import com.chbachman.cminus.representation.value.Variable;
import com.chbachman.cminus.util.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Chandler on 4/15/17.
 */
public class Struct implements CodeBlock, Typed {

    String name;
    public final List<Variable> variables = new ArrayList<>();

    public Struct(CMinusParser.StructContext ctx, Scope scope) {
        this.name = ctx.ID().getText();
        // TODO: init / deinit blocks? How will deinit work?
    }

    Lazy<CreatedFunction> initFunc = new Lazy();

    public CreatedFunction initFunc(Scope scope) {
        if (initFunc.lazy()) {
            return initFunc.get();
        }

        CreatedFunction f = new CreatedFunction(type(), "init" + name);

        Variable temp = new Variable("created" + name, type());

        f.addStatement(temp);

        for(Variable v: variables) {
            if (v.value.isPresent()) {
                f.addStatement(new Assignment(temp.name + "." + v.name, v.value.get()));
            }
        }

        // TODO: Actual init of variables of struct here.

        f.addStatement(new Return(temp));

        return initFunc.create(f);
    }

    public Optional<Variable> getVariable(String name) {
        for (Variable variable : variables) {
            if (variable.name.equals(name)) {
                return Optional.of(variable);
            }
        }

        return Optional.empty();
    }

    @Override
    public String first() {
        return "struct " + name + " {";
    }

    @Override
    public String last() {
        return "};\n";
    }

    @Override
    public Type type() {
        return Type.from(this);
    }
}

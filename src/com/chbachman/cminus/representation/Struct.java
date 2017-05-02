package com.chbachman.cminus.representation;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.function.CodeBlock;
import com.chbachman.cminus.representation.function.CreatedFunction;
import com.chbachman.cminus.representation.function.Function;
import com.chbachman.cminus.representation.function.ParameterList;
import com.chbachman.cminus.representation.statement.Assignment;
import com.chbachman.cminus.representation.statement.Return;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Value;
import com.chbachman.cminus.representation.value.Variable;
import com.chbachman.cminus.util.Lazy;

import java.util.*;

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

    public final Set<CreatedFunction> inits = new TreeSet<>();

    public Function createInit(CMinusParser.InitBlockContext ctx, Scope scope) {
        List<Variable> parameters = ParameterList.parse(ctx.parameterList());
        {
            Optional<Function> function = getInit(parameters);
            if (getInit(parameters).isPresent()) {
                return function.get();
            }
        }

        CreatedFunction f = new CreatedFunction(type(), "init" + name);

        Variable temp = new Variable("created" + name, type());

        f.parameters.clear();
        f.parameters.addAll(parameters);

        f.addStatement(temp);

        // Add all the initialization of variables.
        for(Variable v: variables) {
            if (v.value.isPresent()) {
                f.addStatement(new Assignment(temp.name + "." + v.name, v.value.get()));
            }
        }

        f.addStatement(new Return(temp));

        inits.add(f);

        return f;
    }

    public Optional<Function> getInit(List<? extends Typed> parameters) {
        for (CreatedFunction init: inits) {
            if (init.matches(parameters)) {
                return Optional.of(init);
            }
        }

        return Optional.empty();
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
    public String middle() {
        return "";
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

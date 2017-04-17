package com.chbachman.cminus.representation;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.function.CodeBlock;
import com.chbachman.cminus.representation.function.CreatedFunction;
import com.chbachman.cminus.representation.function.Function;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chandler on 4/15/17.
 */
public class Struct implements CodeBlock {

    String name;
    public final List<Variable> variables = new ArrayList<>();

    public Struct(CMinusParser.StructContext ctx, Scope scope) {
        this.name = ctx.ID().getText();
    }

    public Function initFunc() {
        CreatedFunction f = new CreatedFunction(Type.from(this), "init" + name);

        for (Variable var: variables) {

        }

        return f;
    }

    @Override
    public String first() {
        return "struct " + name + " {";
    }

    @Override
    public String last() {
        return "};\n";
    }
}

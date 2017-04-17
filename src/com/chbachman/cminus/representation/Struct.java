package com.chbachman.cminus.representation;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.function.CodeBlock;
import com.chbachman.cminus.representation.function.CreatedFunction;
import com.chbachman.cminus.representation.function.Function;
import com.chbachman.cminus.representation.statement.Assignment;
import com.chbachman.cminus.representation.statement.Return;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.List;

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

    public CreatedFunction initFunc(Scope scope) {
        CreatedFunction f = new CreatedFunction(type(), "init" + name);

        Variable temp = new Variable("created" + name, type());

        f.addStatement(temp);
        // TODO: Actual init of variables of struct here.
        f.addStatement(new Return(temp));

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

    @Override
    public Type type() {
        return Type.from(this);
    }
}

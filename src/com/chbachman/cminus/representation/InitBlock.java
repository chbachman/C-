package com.chbachman.cminus.representation;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.function.*;
import com.chbachman.cminus.representation.statement.Assignment;
import com.chbachman.cminus.representation.statement.Return;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chandler on 5/5/17.
 */
public class InitBlock extends Function {

    public InitBlock(CMinusParser.InitBlockContext ctx, Struct parent, Scope scope) {
        super(parent.type(), "init" + parent.name, new ArrayList<>());
        parameters = ParameterList.parse(ctx.parameterList());

        scope.pushScope(this);

        // Create the temp variable to return.
        Variable toInit = new Variable("created" + parent.name, parent.type());
        scope.setThis(toInit, parent);
        this.statements.add(toInit);

        // Add the declared variables if declared inline.
        for(Variable v: parent.variables) {
            if (v.value.isPresent()) {
                this.statements.add(new Assignment(toInit.name + "." + v.name, v.value.get()));
            }
        }

        // Create the rest of the codeblock.
        this.statements.addAll(CodeBlock.parse(ctx.codeBlock(), scope));

        // Add the return value.
        this.statements.add(new Return(toInit));

        scope.popScope();
    }

    @Override
    public void setupScope(Scope scope) {
        for(Variable v : parameters) {
            scope.addVariable(v);
        }
    }
}

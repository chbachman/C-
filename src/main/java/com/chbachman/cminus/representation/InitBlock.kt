package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.function.CodeBlock
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.function.ParameterList
import com.chbachman.cminus.representation.statement.Assignment
import com.chbachman.cminus.representation.statement.Return
import com.chbachman.cminus.representation.value.Variable

/**
 * Created by Chandler on 5/5/17.
 * Representation of Init Block.
 */
class InitBlock(ctx: CMinusParser.InitBlockContext, parent: Struct, scope: Scope) : Function(parent.type, "init" + parent.name, ParameterList.parse(ctx.parameterList())) {

    init {

        scope.pushScope(this)

        // Create the temp variable to return.
        val toInit = Variable("created" + parent.name, parent.type)
        scope.setThis(toInit, parent)
        this.statements.add(toInit)

        // Add the declared variables if declared inline.
        for (v in parent.getVariables()) {
            this.statements.add(Assignment(toInit.name + "." + v.name, v.value))
        }

        // Create the rest of the codeblock.
        this.statements.addAll(CodeBlock.Companion.parse(ctx.codeBlock(), scope))

        // Add the return value.
        this.statements.add(Return(toInit))

        scope.popScope()
    }

    override fun setupScope(scope: Scope) {
        for (v in this.parameters) {
            scope.addVariable(v)
        }
    }
}

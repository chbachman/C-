package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.function.FunctionType
import com.chbachman.cminus.representation.function.Header
import com.chbachman.cminus.representation.statement.Assignment
import com.chbachman.cminus.representation.statement.Return
import com.chbachman.cminus.representation.value.Statement
import com.chbachman.cminus.representation.value.Variable

/**
 * Created by Chandler on 5/5/17.
 * Representation of Init Block.
 */
class InitBlock(override val header: InitHeader, parent: Struct, scope: Scope) : FunctionType() {
    override val statements: List<Statement>

    init {
        scope.pushScope(this)

        val statements: MutableList<Statement> = mutableListOf()

        // Create the temp variable to return.
        val toInit = Variable("created${parent.name}", type)
        scope.setThis(toInit, parent)
        statements.add(toInit)

        // Add the declared variables if declared inline.
        parent.variables.forEach {
            if (it.value != null) {
                statements.add(Assignment(toInit.name + "." + it.name, it.value))
            }
        }

        // Create the rest of the code block.
        statements.addAll(Parser.parse(header.ctx.codeBlock(), scope))

        // Add the return value.
        statements.add(Return(toInit))

        this.statements = statements

        scope.popScope()
    }
}

class InitHeader(val ctx: CMinusParser.InitBlockContext, val parent: StructHeader) : Header() {
    val name = parent.name
    override val baseName = "init$name"
    override val type = parent.type
    override val parameters = ctx.parameterList()?.let { Parser.parse(it) } ?: emptyList()

    override fun getFunc(scope: Scope): InitBlock {
        return InitBlock(this, parent.getStruct(scope), scope)
    }
}

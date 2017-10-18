package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.function.FunctionType
import com.chbachman.cminus.representation.function.Header
import com.chbachman.cminus.representation.statement.Assignment
import com.chbachman.cminus.representation.statement.Return
import com.chbachman.cminus.representation.value.Identifier
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

        // Create the temp variable to return.
        val toInit = Variable(Identifier("created${parent.name}"), type)
        scope.setThis(toInit, parent)

        this.statements = listOf<Statement>(toInit) +
        parent.variables.mapNotNull {
            Assignment(toInit.name + it.name, it)
        } +
        Parser.parse(header.ctx.codeBlock(), scope) +
        listOf(Return(toInit))

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

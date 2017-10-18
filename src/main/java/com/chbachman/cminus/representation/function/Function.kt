package com.chbachman.cminus.representation.function

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.VariableHolder
import com.chbachman.cminus.representation.struct.StructHeader
import com.chbachman.cminus.representation.value.Identifier
import com.chbachman.cminus.representation.value.Statement
import com.chbachman.cminus.representation.value.Variable

/**
 * Created by Chandler on 5/20/17.
 * Represents the actual function type (Basically FunctionContext -> FunctionType)
 */
class Function(override val header: FunctionHeader, scope: Scope) : FunctionType() {
    override val statements: List<Statement>

    init {
        scope.pushScope(this)
        if (header.self != null) {
            scope.setThis(header.self!!, header.getHolder(scope)!!)
        }
        statements = Parser.parse(header.ctx.codeBlock(), scope)
        scope.popScope()
    }
}

open class FunctionHeader(val ctx: CMinusParser.FuncContext): Header() {
    override val baseName: String = ctx.ID().text
    override val parameters = ctx.parameterList()?.let { it.parameter().map { Variable(it) }  } ?: emptyList()
    override val type = if (ctx.funcReturn() != null) Type[ctx.funcReturn().type()] else Type.Native.VOID.type

    override fun getFunc(scope: Scope): Function {
        return Function(this, scope)
    }

    // Allows for the creation of a "self" variable.
    open val self: Variable? = null
    open fun getHolder(scope: Scope): VariableHolder? {
        return null
    }
}

class StructFunctionHeader(ctx: CMinusParser.FuncContext, val parent: StructHeader) : FunctionHeader(ctx) {
    override val self = Variable(Identifier("self"), parent.type)

    override val parameters = listOf(self) + super.parameters
    override val baseName = "${parent.name}\$${super.baseName}"

    override fun getHolder(scope: Scope): VariableHolder? {
        return parent.getStruct(scope)
    }

}
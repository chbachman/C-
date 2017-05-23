package com.chbachman.cminus.representation.function

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
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
        statements = Parser.parse(header.ctx.codeBlock(), scope)
        scope.popScope()
    }
}

class FunctionHeader(val ctx: CMinusParser.FuncContext): Header() {
    override val baseName: String = ctx.ID().text
    override val parameters = ctx.parameterList()?.let { it.parameter().map { Variable(it) }  } ?: emptyList()
    override val type = if (ctx.funcReturn() != null) Type.from(ctx.funcReturn().type()) else Type.Native.VOID.type

    override fun getFunc(scope: Scope): Function {
        return Function(this, scope)
    }
}
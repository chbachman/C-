package com.chbachman.cminus.representation.control

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.function.CodeBlock
import com.chbachman.cminus.representation.function.CodeBlockHolder
import com.chbachman.cminus.representation.statement.Statement
import com.chbachman.cminus.representation.value.Value
import com.chbachman.cminus.representation.value.Variable

/**
 * Created by Chandler on 4/14/17.
 */
class ForStatement(ctx: CMinusParser.ForStatementContext, scope: Scope) : CodeBlockHolder(), Control {

    private val minimum: Value
    private val maximum: Value

    private val index: Variable

    override val statements: List<Statement>

    init {
        val range = ctx.range()

        minimum = Parser.parse(range.value(0), scope)
        maximum = Parser.parse(range.value(1), scope)

        if (minimum.type != Type.Native.INT.type || maximum.type != Type.Native.INT.type) {
            throw RuntimeException("For Loop without int range. ${range.text}")
        }

        index = Variable(range.ID().text, Type.Native.INT.type)

        scope.pushScope(this)
        statements = Parser.parse(ctx.codeBlock(), scope)
        scope.popScope()
    }

    override fun setupScope(scope: Scope) {
        scope.addVariable(index)
    }

    override val first: String
        get() {
            val i = index.name
            return "for (int $i = ${minimum.value()}; $i <= ${maximum.value()}; $i++) {"
        }

    override val last = "}"
}
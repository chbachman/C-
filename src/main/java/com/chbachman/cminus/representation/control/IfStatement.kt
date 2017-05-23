package com.chbachman.cminus.representation.control

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.function.CodeBlockHolder

/**
 * Created by Chandler on 4/14/17.
 * Handles If Statements
 */
class IfStatement(ctx: CMinusParser.IfStatementContext, scope: Scope) : CodeBlockHolder(), Control {

    val value = Parser.parse(ctx.value(0), scope)
    override val statements = Parser.parse(ctx.codeBlock()[0], scope)

    override val first = "if (${value.expression}) {"
    override val last = "}"
}

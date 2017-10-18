package com.chbachman.cminus.representation.value

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope

/**
 * Created by Chandler on 4/14/17.
 * Handles Parenthesis around values.
 */
class Paren(ctx: CMinusParser.ValueContext, scope: Scope) : Expression {
    private val value = Parser.parse(ctx.value(0), scope)

    override val type = value.type
    override val expression = "(${value.expression})"
}

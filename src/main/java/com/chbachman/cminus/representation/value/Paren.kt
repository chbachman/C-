package com.chbachman.cminus.representation.value

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type

/**
 * Created by Chandler on 4/14/17.
 * Handles Parenthesis around values.
 */
class Paren(ctx: CMinusParser.ValueContext, scope: Scope) : Value {
    private val value = Parser.parse(ctx.value(0), scope)

    override val type = value.type

    override fun value(): String {
        return "(${value.value()})"
    }
}

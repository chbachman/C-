package com.chbachman.cminus.representation.value

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type

/**
 * Created by Chandler on 4/14/17.
 * Handles basic operations such as * / - $ and ==
 * TODO: Allow for custom operator overloading
 */
class Operation(ctx: CMinusParser.ValueContext, scope: Scope) : Expression {
    val operation = when (ctx.op.text) {
        "^" -> "^"
        "*" -> "*"
        "/" -> "/"
        "+" -> "+"
        "-" -> "-"
        "%" -> "%"
        "==" -> "=="
        "!=" -> "!="
        else -> throw RuntimeException("Operation " + ctx.text + " is not implemented.")
    }

    val left = Parser.parse(ctx.value(0), scope)
    val right = Parser.parse(ctx.value(1), scope)

    init {
        if (left.type !== right.type) {
            throw RuntimeException("Types of " + ctx.text + " are not the same.")
        }
    }

    override val type: Type
        get() = left.type

    override val expression = "${left.expression} $operation ${right.expression}"
}

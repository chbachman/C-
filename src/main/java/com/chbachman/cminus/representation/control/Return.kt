package com.chbachman.cminus.representation.control

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Parser

// We don't need to do any special Return checking
// C will handle all that.
class Return(ctx: Kotlin.JumpExpressionContext): Expression {
    val expression = Parser.parse(ctx.expression())
    override val type = expression.type

    override fun toString(): String {
        return "return $expression"
    }
}
package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin

class DotExpression(ctx: Kotlin.DotQualifiedExpressionContext, exp: Boolean): Expression {
    override val type: Type
    val primary = Parser.parse(ctx.assignableExpression())
    val second: Expression

    init {
        second = Parser.parse(primary, ctx.postfixUnaryExpression().first(), exp)
        type = second.type
    }

    override fun toString(): String {
        return "$primary->$second"
    }

}
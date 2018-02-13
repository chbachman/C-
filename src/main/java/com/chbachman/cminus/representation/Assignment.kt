package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin

class Assignment(ctx: Kotlin.AssignmentContext): Expression {
    val left = Parser.parse(ctx.assignableExpression())
    val right = Parser.parse(ctx.disjunction())

    override val type: Type
        get() = right.type

    override fun toString(): String {
        return "$left = $right"
    }
}
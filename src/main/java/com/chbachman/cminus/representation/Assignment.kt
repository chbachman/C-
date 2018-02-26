package com.chbachman.cminus.representation

import com.chbachman.cminus.Variable
import com.chbachman.cminus.gen.Kotlin

class Assignment(ctx: Kotlin.AssignmentContext): Expression {
    val left = Parser.parse(ctx.assignableExpression())
    val right = Parser.parse(ctx.disjunction())

    init {
        if (left is Variable) {
            left.set()
        }

        if (right is Variable) {
            right.get()
        }
    }

    override val type: Type
        get() = right.type

    override fun toString(): String {
        return "$left = $right"
    }
}
package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin

class Equality(ctx: Kotlin.EqualityContext): Expression {
    val members = ctx.comparison().map { Parser.parse(it) }
    val operands = ctx.equalityOperator().map { it.text }
    override val type = Type.Native.Int

    override fun toString(): String {
        var i = 0
        return members.map { it.toString() }.reduce { s1, s2 ->
            // i++ to get the correct operand for the reduce operation, since each operation will use one up.
            "$s1 ${operands[i++]} $s2"
        }
    }
}
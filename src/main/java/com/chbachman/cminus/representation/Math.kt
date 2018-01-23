package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin

// TODO: Fix Division/Subtraction/Modulo
class Addition(ctx: Kotlin.AdditiveExpressionContext): Expression {
    val members = ctx.multiplicativeExpression().map { Parser.parse(it) }
    val operands = ctx.additiveOperator().map { it.text }
    override val type = Type.Native.INT.type

    override fun toString(): String {
        var i = 0
        return members.map { it.toString() }.reduce { s1, s2 ->
            "$s1 ${operands[i++]} $s2"
        }
    }
}

class Multiplication(ctx: Kotlin.MultiplicativeExpressionContext): Expression {
    val members = ctx.asExpression().map { Parser.parse(it) }
    val operands = ctx.multiplicativeOperator().map { it.text }
    override val type = Type.Native.INT.type

    override fun toString(): String {
        var i = 0
        return members.map { it.toString() }.reduce { s1, s2 ->
            // i++ to get the correct operand for the reduce operation, since each operation will use one up.
            "$s1 ${operands[i++]} $s2"
        }
    }
}
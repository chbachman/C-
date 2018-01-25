package com.chbachman.cminus.representation

import com.chbachman.cminus.ScopeStack
import com.chbachman.cminus.gen.Kotlin

abstract class FunctionCall(ctx: Kotlin.CallExpressionContext): Expression {
    val name = getName(ctx)
    abstract val fullName: String
    val parameters = ctx
        .valueArguments()
        .valueArgument()
        .map { Parser.parse(it.expression()) }

    protected fun parameterList(): String {
        return parameters.map { "$it" }.reduce { e1, e2 ->
            "$e1, $e2"
        }
    }

    override fun toString(): String {
        return "$fullName(${parameterList()})"
    }

    companion object {
        fun parse(ctx: Kotlin.CallExpressionContext): FunctionCall {
            return when {
                getName(ctx) == "print" -> PrintCall(ctx, false)
                getName(ctx) == "println" -> PrintCall(ctx, true)
                else -> NormalCall(ctx)
            }
        }

        private fun getName(ctx: Kotlin.CallExpressionContext): String {
            return ctx.assignableExpression().primaryExpression().simpleIdentifier().text
        }
    }
}

private class NormalCall(ctx: Kotlin.CallExpressionContext): FunctionCall(ctx) {
    override val type: Type
    override val fullName: String

    init {
        val possibleFunctions = ScopeStack.getFunc(name, parameters.map { it.type })

        if (possibleFunctions.isEmpty()) {
            throw RuntimeException("Function $name(${parameterList()}) does not exist.")
        }

        if (possibleFunctions.size != 1) {
            throw RuntimeException(
                "Function $name(${parameterList()}) has multiple valid overloads: $possibleFunctions"
            )
        }

        val function = possibleFunctions.first()

        fullName = function.fullName
        type = function.returnType
    }
}

private class PrintCall(ctx: Kotlin.CallExpressionContext, val newline: Boolean): FunctionCall(ctx) {
    override val fullName = "printf"
    override val type = Type.Native.VOID.type

    override fun toString(): String {
        val newline = if (newline) "\\n" else ""
        val formatString = "\"" + formatString(parameters.first()) + newline + "\""

        return "$fullName($formatString, ${parameterList()})"
    }

    fun formatString(typed: Typed): String {
        val type = typed.type

        return when (type) {
            Type.Native.CHAR.type -> "%c"
            Type.Native.INT.type -> "%d"
            Type.Native.CSTRING.type -> "%s"
            Type.Native.FLOAT.type -> "%f"
            else -> TODO("Waiting for toString implementation here")
        }
    }
}
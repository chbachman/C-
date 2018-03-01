package com.chbachman.cminus.representation.function

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.Typed
import com.chbachman.cminus.representation.struct.ConstructorCall

abstract class FunctionCall(ctx: Kotlin.CallExpressionContext): Expression {
    val name = getName(ctx)
    abstract val fullName: String
    open val parameters = ctx
        .valueArguments()
        .valueArgument()
        .map { Parser.parse(it.expression()) }

    protected fun parameterList(): String {
        return parameters.joinToString { it.toString() }
    }

    override fun toString(): String {
        return "$fullName(${parameterList()})"
    }

    companion object {
        // Decide what the function call is and get the proper call.
        fun parse(parent: Expression, ctx: Kotlin.CallExpressionContext): FunctionCall {
            return ClassCall(parent, ctx)
        }

        fun parse(ctx: Kotlin.CallExpressionContext): FunctionCall {
            val name = getName(ctx)
            val possibleType = Type[getName(ctx)]

            return when {
                name == "print" -> PrintCall(ctx, false)
                name == "println" -> PrintCall(ctx, true)
                possibleType != null -> ConstructorCall(possibleType, ctx)
                else -> NormalCall(ctx)
            }
        }

        private fun getName(ctx: Kotlin.CallExpressionContext): String {
            return ctx.assignableExpression().primaryExpression().simpleIdentifier().text
        }
    }
}

private class ClassCall(val parent: Expression, ctx: Kotlin.CallExpressionContext): FunctionCall(ctx) {
    override val fullName: String
    override val type: Type
    override val parameters: List<Expression>

    init {
        val table = SymbolTable[parent.type]!!
        val parameters = listOf(parent.type) + super.parameters.map { it.type }
        val possibleFunctions = table[name, parameters]

        if (possibleFunctions.isEmpty()) {
            throw RuntimeException("Function $name(${parameterList()}) does not exist.")
        }

        if (possibleFunctions.size != 1) {
            throw RuntimeException(
                "Function $name(${parameterList()}) has multiple valid overloads: $possibleFunctions"
            )
        }

        val function = possibleFunctions.first()

        fullName = function.name
        type = function.returnType
        this.parameters = listOf(parent) + super.parameters
    }
}

private class NormalCall(ctx: Kotlin.CallExpressionContext): FunctionCall(ctx) {
    override val type: Type
    override val fullName: String

    init {
        val possibleFunctions = SymbolTable[name, parameters.map { it.type }]

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
    override val type = Type.Native.Unit

    override fun toString(): String {
        val newline = if (newline) "\\n" else ""
        val formatString = "\"" + parameters.joinToString { formatString(it) } + newline + "\""

        val parameterList = parameters.joinToString { param ->
            if (param.type == Type.Native.Boolean) {
                "$param ? \"true\" : \"false\""
            } else {
                param.toString()
            }
        }

        return "$fullName($formatString, $parameterList)"
    }

    fun formatString(typed: Typed): String {
        val type = typed.type

        return when (type) {
            Type.Native.Char -> "%c"
            Type.Native.Int -> "%d"
            Type.Native.CString -> "%s"
            Type.Native.Float -> "%f"
            Type.Native.Boolean -> "%s"
            else -> TODO("Waiting for toString implementation here")
        }
    }
}
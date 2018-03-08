package com.chbachman.cminus.representation.function

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.Typed
import com.chbachman.cminus.representation.struct.ConstructorCall

abstract class FunctionCall(ctx: Kotlin.CallExpressionContext): Expression {
    // The name of the function in Kotlin
    val name = getName(ctx)

    // The name of the function in C
    // Can be different due to namespace/object/overloading.
    abstract val fullName: String

    // The parameters, in order.
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
        // The only parent we have will be a call to a class method.
        fun parse(parent: Expression, ctx: Kotlin.CallExpressionContext): FunctionCall {
            return ClassCall(parent, ctx)
        }

        // Decide what the function call is and get the proper call.
        fun parse(ctx: Kotlin.CallExpressionContext): FunctionCall {
            val name = getName(ctx)
            val possibleType = Type[getName(ctx)]

            return when {
                name == "print" -> PrintCall(ctx, false)
                name == "println" -> PrintCall(ctx, true)
                possibleType != null -> ConstructorCall(possibleType, ctx)
                else -> parseNormal(name, ctx)
            }
        }

        // At this point, it should be a function in some scope.
        // The Normal Call should be able to parse, but it might not transform it correctly.
        private fun parseNormal(
            name: String,
            ctx: Kotlin.CallExpressionContext
        ): FunctionCall {
            val call = NormalCall(ctx)

            // We have an Inline Call
            if (call.function.inline) {
                return InlineCall(call, ctx)
            }

            return call
        }

        private fun getName(ctx: Kotlin.CallExpressionContext): String {
            return ctx.assignableExpression().primaryExpression().simpleIdentifier().text
        }
    }
}

// Inline call, will actually contain body of call. (Hopefully short)
// Prevents function calls when you don't need it.
private class InlineCall(call: NormalCall, ctx: Kotlin.CallExpressionContext): FunctionCall(ctx) {
    override val fullName: String = call.fullName
    override val type: Type = call.type

    val body = call.function.parse().block

    override fun toString(): String {
        return body.toString()
    }
}

// Call of a method.
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

// A normal call to a global function.
private class NormalCall(ctx: Kotlin.CallExpressionContext): FunctionCall(ctx) {
    override val type: Type
    override val fullName: String
    val function: Header<Function>

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

        function = possibleFunctions.first()

        fullName = function.fullName
        type = function.returnType
    }
}

// A call to print()
// We have to resolve this to printf() and that takes some call wrangling.
private class PrintCall(ctx: Kotlin.CallExpressionContext, val newline: Boolean): FunctionCall(ctx) {
    override val fullName = "printf"
    override val type = Type.Native.Unit

    override fun toString(): String {
        val newline = if (newline) "\\n" else ""
        val formatString = "\"" + parameters.joinToString { formatString(it) } + newline + "\""

        val parameterList = parameters.joinToString { param ->
            // Handle special cases for different types.
            when (param.type) {
                Type.Native.Boolean -> "$param ? \"true\" : \"false\""
                Type.Native.Nothing -> "\"null\""
                else -> param.toString()
            }
        }

        return "$fullName($formatString, $parameterList)"
    }

    fun formatString(typed: Typed): String {
        val type = typed.type

        return when (type) {
            Type.Native.Char -> "%c"
            Type.Native.Int -> "%d"
            Type.Native.Long -> "%ld"
            Type.Native.CString -> "%s"
            Type.Native.Float -> "%g"
            Type.Native.Double -> "%lg"
            Type.Native.Boolean -> "%s"
            Type.Native.Nothing -> "%s"
            else -> TODO("Waiting for toString implementation here")
        }
    }
}
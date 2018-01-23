package com.chbachman.cminus.representation

import com.chbachman.cminus.ScopeStack
import com.chbachman.cminus.gen.Kotlin

class FunctionCall(ctx: Kotlin.CallExpressionContext): Expression {
    override val type: Type
    val name = ctx.assignableExpression().primaryExpression().simpleIdentifier().text
    val fullName: String
    val parameters: List<Expression>

    init {
        parameters = ctx.valueArguments().valueArgument().map { Parser.parse(it.expression()) }
        val possibleFunctions = ScopeStack.getFunc(name, parameters.map { it.type })

        if (possibleFunctions.size == 0) {
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

    private fun parameterList(): String {
        return parameters.map { "$it" }.reduce { e1, e2 ->
            "$e1, $e2"
        }
    }

    override fun toString(): String {
        return "$fullName(${parameterList()})"
    }
}
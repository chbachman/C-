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
        val function = ScopeStack.getFunc(name, parameters.map { it.type }) ?:
            throw RuntimeException("Function doesn't exist.")

        fullName = function.fullName
        type = function.returnType
    }

    override fun toString(): String {
        return "$fullName(" + parameters.map { "$it" }.reduce { e1, e2 ->
            "$e1, $e2"
        } + ")"
    }
}
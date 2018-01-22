package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.literal.StringLiteral

/**
 * Created by Chandler on 5/17/17.
 * Handles Parsing of Different types of Contexts
 */
object Parser {

    fun parse(ctx: Kotlin.TopLevelObjectContext): TopLevel {
        when {
            ctx.functionDeclaration() != null -> return Func(ctx.functionDeclaration())
        }

        throw RuntimeException("The function type: " + ctx.text + " is not implemented yet.")
    }

    fun parse(ctx: Kotlin.StatementContext): Statement {
        when {
            ctx.expression() != null -> return parse(ctx.expression())
        }

        throw RuntimeException("The statement type: " + ctx.text + " is not implemented yet.")
    }

    fun parse(ctx: Kotlin.BlockContext): CodeBlock {
        return CodeBlock(ctx)
    }

    // MARK: Expression Parsing Fun! ʕ •ᴥ•ʔ
    fun parse(ctx: Kotlin.ExpressionContext): Expression {
        return parse(ctx.disjunction())
    }

    fun parse(ctx: Kotlin.DisjunctionContext): Expression {
        if (ctx.conjunction().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.conjunction().first())
    }

    fun parse(ctx: Kotlin.ConjunctionContext): Expression {
        if (ctx.equality().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.equality().first())
    }

    fun parse(ctx: Kotlin.EqualityContext): Expression {
        if (ctx.comparison().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.comparison().first())
    }

    fun parse(ctx: Kotlin.ComparisonContext): Expression {
        if (ctx.infixOperation().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.infixOperation().first())
    }

    fun parse(ctx: Kotlin.InfixOperationContext): Expression {
        if (ctx.elvisExpression().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.elvisExpression().first())
    }

    fun parse(ctx: Kotlin.ElvisExpressionContext): Expression {
        if (ctx.infixFunctionCall().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.infixFunctionCall().first())
    }

    fun parse(ctx: Kotlin.InfixFunctionCallContext): Expression {
        if (ctx.rangeExpression().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.rangeExpression().first())
    }

    fun parse(ctx: Kotlin.RangeExpressionContext): Expression {
        if (ctx.additiveExpression().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.additiveExpression().first())
    }

    fun parse(ctx: Kotlin.AdditiveExpressionContext): Expression {
        if (ctx.multiplicativeExpression().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.multiplicativeExpression().first())
    }

    fun parse(ctx: Kotlin.MultiplicativeExpressionContext): Expression {
        if (ctx.asExpression().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.asExpression().first())
    }

    fun parse(ctx: Kotlin.AsExpressionContext): Expression {
        if (ctx.asExpressionTail() != null) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.prefixUnaryExpression())
    }

    fun parse(ctx: Kotlin.PrefixUnaryExpressionContext): Expression {
        if (ctx.prefixUnaryOperator().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        if (ctx.annotations().size > 1) {
            throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.postfixUnaryExpression())
    }

    fun parse(ctx: Kotlin.PostfixUnaryExpressionContext): Expression {
        return when {
            ctx.callExpression() != null -> FunctionCall(ctx.callExpression())
            ctx.assignableExpression() != null -> parse(ctx.assignableExpression())
            else -> throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.AssignableExpressionContext): Expression {
        return when {
            ctx.primaryExpression() != null -> Parser.parse(ctx.primaryExpression())
            else -> throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.PrimaryExpressionContext): Expression {
        return when {
            ctx.stringLiteral() != null -> StringLiteral(ctx.stringLiteral())
            ctx.simpleIdentifier() != null -> VariableRef(ctx.simpleIdentifier())
            else -> throw RuntimeException("The value type: " + ctx.text + " is not implemented yet.")
        }
    }
}

interface TopLevel

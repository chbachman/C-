package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.literal.*

/**
 * Created by Chandler on 5/17/17.
 * Handles Parsing of Different types of Contexts
 */
object Parser {

    fun parse(ctx: Kotlin.TopLevelObjectContext): TopLevel {
        when {
            ctx.functionDeclaration() != null -> {
                val func = ctx.functionDeclaration()

                if (func.modifierList() != null) {
                    if (func.modifierList().modifier().any {
                        it.functionModifier()?.EXTERNAL() != null
                    }) {
                        return NativeFunc(ctx.functionDeclaration())
                    }
                }

                return DeclaredFunc(ctx.functionDeclaration())
            }
        }

        TODO("The function type: " + ctx.text + " is not implemented yet.")
    }

    fun parse(ctx: Kotlin.StatementContext): Statement {
        return when {
            ctx.expression() != null -> parse(ctx.expression())
            ctx.declaration() != null -> parse(ctx.declaration())
            else -> TODO("The statement type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.DeclarationContext): Statement {
        return when {
            ctx.propertyDeclaration() != null -> Property(ctx.propertyDeclaration())
            else -> TODO("The statement type: " + ctx.text + " is not implemented yet.")
        }
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
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.conjunction().first())
    }

    fun parse(ctx: Kotlin.ConjunctionContext): Expression {
        if (ctx.equality().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.equality().first())
    }

    fun parse(ctx: Kotlin.EqualityContext): Expression {
        if (ctx.comparison().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.comparison().first())
    }

    fun parse(ctx: Kotlin.ComparisonContext): Expression {
        if (ctx.infixOperation().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.infixOperation().first())
    }

    fun parse(ctx: Kotlin.InfixOperationContext): Expression {
        if (ctx.elvisExpression().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.elvisExpression().first())
    }

    fun parse(ctx: Kotlin.ElvisExpressionContext): Expression {
        if (ctx.infixFunctionCall().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.infixFunctionCall().first())
    }

    fun parse(ctx: Kotlin.InfixFunctionCallContext): Expression {
        if (ctx.rangeExpression().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.rangeExpression().first())
    }

    fun parse(ctx: Kotlin.RangeExpressionContext): Expression {
        if (ctx.additiveExpression().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.additiveExpression().first())
    }

    fun parse(ctx: Kotlin.AdditiveExpressionContext): Expression {
        // If we only have one, there isn't any multiplication to do.
        if (ctx.multiplicativeExpression().size > 1) {
            return Addition(ctx)
        }

        return parse(ctx.multiplicativeExpression().first())
    }

    fun parse(ctx: Kotlin.MultiplicativeExpressionContext): Expression {
        // If we only have one, there isn't any multiplication to do.
        if (ctx.asExpression().size > 1) {
            return Multiplication(ctx)
        }

        return parse(ctx.asExpression().first())
    }

    fun parse(ctx: Kotlin.AsExpressionContext): Expression {
        if (ctx.asExpressionTail() != null) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.prefixUnaryExpression())
    }

    fun parse(ctx: Kotlin.PrefixUnaryExpressionContext): Expression {
        if (ctx.prefixUnaryOperator().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        if (ctx.annotations().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.postfixUnaryExpression())
    }

    fun parse(ctx: Kotlin.PostfixUnaryExpressionContext): Expression {
        return when {
            ctx.callExpression() != null -> FunctionCall.parse(ctx.callExpression())
            ctx.assignableExpression() != null -> parse(ctx.assignableExpression())
            else -> TODO("The value type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.AssignableExpressionContext): Expression {
        return when {
            ctx.primaryExpression() != null -> Parser.parse(ctx.primaryExpression())
            else -> TODO("The value type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.PrimaryExpressionContext): Expression {
        return when {
            ctx.stringLiteral() != null -> StringLiteral(ctx.stringLiteral())
            ctx.simpleIdentifier() != null -> VariableRef(ctx.simpleIdentifier())
            ctx.literalConstant() != null -> parse(ctx.literalConstant())
            else -> TODO("The value type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.LiteralConstantContext): Expression {
        return when {
            ctx.BinLiteral() != null -> BinLiteral(ctx.BinLiteral())
            ctx.IntegerLiteral() != null -> IntegerLiteral(ctx.IntegerLiteral())
            ctx.HexLiteral() != null -> HexLiteral(ctx.HexLiteral())
            ctx.BooleanLiteral() != null -> BooleanLiteral(ctx.BooleanLiteral())
            ctx.CharacterLiteral() != null -> CharLiteral(ctx.CharacterLiteral())
            ctx.RealLiteral() != null -> RealLiteral.parse(ctx.RealLiteral())
            ctx.NullLiteral() != null -> NullLiteral(ctx.NullLiteral())
            ctx.LongLiteral() != null -> LongLiteral(ctx.LongLiteral())
            else -> TODO("The value type: " + ctx.text + " is not implemented yet.")
        }
    }
}

interface TopLevel

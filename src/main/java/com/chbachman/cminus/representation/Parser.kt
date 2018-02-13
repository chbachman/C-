package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.control.IfExpression
import com.chbachman.cminus.representation.function.DeclaredFunc
import com.chbachman.cminus.representation.function.FunctionCall
import com.chbachman.cminus.representation.function.NativeFunc
import com.chbachman.cminus.representation.literal.*
import com.chbachman.cminus.representation.struct.ClassDeclaration

/**
 * Created by Chandler on 5/17/17.
 * Handles Parsing of Different types of Contexts
 */
object Parser {

    fun parse(ctx: Kotlin.TopLevelObjectContext): TopLevel {
        return when {
            ctx.functionDeclaration() != null -> {
                val func = ctx.functionDeclaration()

                if (func.modifierList() != null) {
                    if (func.modifierList().modifier().any {
                        it.functionModifier()?.EXTERNAL() != null
                    }) {
                        return NativeFunc(ctx.functionDeclaration())
                    }
                }

                DeclaredFunc(ctx.functionDeclaration())
            }

            ctx.classDeclaration() != null -> {
                ClassDeclaration(ctx.classDeclaration())
            }
            else -> TODO("The function type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.StatementContext): Statement {
        return when {
            ctx.expression() != null -> parse(ctx.expression())
            ctx.declaration() != null -> parse(ctx.declaration())
            ctx.assignment() != null -> Assignment(ctx.assignment())
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
    fun parse(ctx: Kotlin.ExpressionContext, exp: Boolean = true): Expression {
        return parse(ctx.disjunction(), exp)
    }

    fun parse(ctx: Kotlin.DisjunctionContext, exp: Boolean = true): Expression {
        if (ctx.conjunction().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.conjunction().first(), exp)
    }

    fun parse(ctx: Kotlin.ConjunctionContext, exp: Boolean = true): Expression {
        if (ctx.equality().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.equality().first(), exp)
    }

    fun parse(ctx: Kotlin.EqualityContext, exp: Boolean = true): Expression {
        if (ctx.comparison().size > 1) {
            return Equality(ctx)
        }

        return parse(ctx.comparison().first(), exp)
    }

    fun parse(ctx: Kotlin.ComparisonContext, exp: Boolean = true): Expression {
        if (ctx.infixOperation().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.infixOperation().first(), exp)
    }

    fun parse(ctx: Kotlin.InfixOperationContext, exp: Boolean = true): Expression {
        if (ctx.elvisExpression().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.elvisExpression().first(), exp)
    }

    fun parse(ctx: Kotlin.ElvisExpressionContext, exp: Boolean = true): Expression {
        if (ctx.infixFunctionCall().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.infixFunctionCall().first(), exp)
    }

    fun parse(ctx: Kotlin.InfixFunctionCallContext, exp: Boolean = true): Expression {
        if (ctx.rangeExpression().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.rangeExpression().first(), exp)
    }

    fun parse(ctx: Kotlin.RangeExpressionContext, exp: Boolean = true): Expression {
        if (ctx.additiveExpression().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.additiveExpression().first(), exp)
    }

    fun parse(ctx: Kotlin.AdditiveExpressionContext, exp: Boolean = true): Expression {
        // If we only have one, there isn't any multiplication to do.
        if (ctx.multiplicativeExpression().size > 1) {
            return Addition(ctx)
        }

        return parse(ctx.multiplicativeExpression().first(), exp)
    }

    fun parse(ctx: Kotlin.MultiplicativeExpressionContext, exp: Boolean = true): Expression {
        // If we only have one, there isn't any multiplication to do.
        if (ctx.asExpression().size > 1) {
            return Multiplication(ctx)
        }

        return parse(ctx.asExpression().first(), exp)
    }

    fun parse(ctx: Kotlin.AsExpressionContext, exp: Boolean = true): Expression {
        if (ctx.asExpressionTail() != null) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.prefixUnaryExpression(), exp)
    }

    fun parse(ctx: Kotlin.PrefixUnaryExpressionContext, exp: Boolean = true): Expression {
        if (ctx.prefixUnaryOperator().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        if (ctx.annotations().size > 1) {
            TODO("The value type: " + ctx.text + " is not implemented yet.")
        }

        return parse(ctx.postfixUnaryExpression(), exp)
    }

    fun parse(ctx: Kotlin.PostfixUnaryExpressionContext, exp: Boolean = true): Expression {
        return when {
            ctx.callExpression() != null -> FunctionCall.parse(ctx.callExpression())
            ctx.assignableExpression() != null -> parse(ctx.assignableExpression(), exp)
            else -> TODO("The value type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.AssignableExpressionContext, exp: Boolean = true): Expression {
        return when {
            ctx.primaryExpression() != null -> parse(ctx.primaryExpression(), exp)
            else -> TODO("The value type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.PrimaryExpressionContext, exp: Boolean = true): Expression {
        return when {
            ctx.stringLiteral() != null -> StringLiteral(ctx.stringLiteral())
            ctx.simpleIdentifier() != null -> VariableRef(ctx.simpleIdentifier())
            ctx.literalConstant() != null -> parse(ctx.literalConstant(), exp)
            ctx.conditionalExpression() != null -> parse(ctx.conditionalExpression(), exp)
            else -> TODO("The value type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.ConditionalExpressionContext, exp: Boolean = true): Expression {
        return when {
            ctx.ifExpression() != null -> IfExpression(ctx.ifExpression(), exp)
            else -> TODO("The value type: " + ctx.text + " is not implemented yet.")
        }
    }

    fun parse(ctx: Kotlin.LiteralConstantContext, exp: Boolean = true): Expression {
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

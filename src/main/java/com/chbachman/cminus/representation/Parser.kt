package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.control.Control
import com.chbachman.cminus.representation.control.ForStatement
import com.chbachman.cminus.representation.control.IfStatement
import com.chbachman.cminus.representation.statement.*
import com.chbachman.cminus.representation.value.*

/**
 * Created by Chandler on 5/17/17.
 * Handles Parsing of Different types of Contexts
 */
object Parser {

    @JvmStatic
    fun parse(ctx: CMinusParser.ValueContext, scope: Scope): Expression {
        // Regular, non struct variable.
        if (ctx.identifier() != null) {
            val id = Identifier(ctx.identifier(), scope)
            val v = scope.getVariable(id)

            v ?: throw RuntimeException("Invalid Variable: $id")

            return v
        }

        if (ctx.literal() != null) {
            return Literal(ctx.literal())
        }

        // Struct Initialization e.g. Struct() is handled with function call.
        if (ctx.functionCall() != null) {
            return parse(ctx.functionCall(), scope)
        }

        if (ctx.op != null) {
            return Operation(ctx, scope)
        }

        if (ctx.paren != null) {
            return Paren(ctx, scope)
        }

        throw RuntimeException("Type of value isn't implemented. " + ctx.text)
    }

    @JvmStatic
    fun parse(ctx: CMinusParser.StatementContext, scope: Scope): Statement {

        if (ctx.print() != null) {
            return Print(ctx.print(), scope)
        }

        if (ctx.variable() != null) {
            return Variable(ctx.variable(), scope)
        }

        if (ctx.ret() != null) {
            return Return(ctx.ret(), scope)
        }

        if (ctx.assignment() != null) {
            return Assignment(ctx.assignment(), scope)
        }

        if (ctx.functionCall() != null) {
            return parse(ctx.functionCall(), scope)
        }

        if (ctx.control() != null) {
            return parse(ctx.control(), scope)
        }

        throw RuntimeException("The statement type: " + ctx.text + " is not implemented yet.")
    }

    @JvmStatic
    fun parse(ctx: CMinusParser.CodeBlockContext, scope: Scope): List<Statement> {
        return ctx.statement().map { Parser.parse(it, scope) }
    }

    @JvmStatic
    fun parse(ctx: CMinusParser.FunctionCallContext, scope: Scope): Call {
        val name = Identifier(ctx, scope)
        val struct = Type.getStruct(name.text) != null
        val func = scope.functions[name.text]?.isNotEmpty() ?: false

        if (func && struct) {
            throw RuntimeException("There is both a struct and function named: $name")
        }

        if (func) {
            return FunctionCall(ctx, scope)
        }

        if (struct) {
            return StructConstructor(ctx, scope)
        }

        throw RuntimeException("The function type: ${ctx.text} is not implemented yet.")
    }

    @JvmStatic
    fun parse(ctx: CMinusParser.ParameterListContext): List<Variable> {
        return ctx.parameter().map { Variable(it) }
    }

    @JvmStatic
    fun parse(ctx: CMinusParser.ArgumentListContext?, scope: Scope): List<Expression> {
        return ctx?.argument()?.map { Parser.parse(it.value(), scope) } ?: emptyList()
    }

    @JvmStatic
    fun parse(ctx: CMinusParser.ControlContext, scope: Scope): Control {
        if (ctx.ifStatement() != null) {
            return IfStatement(ctx.ifStatement(), scope)
        }

        if (ctx.forStatement() != null) {
            return ForStatement(ctx.forStatement(), scope)
        }

        throw RuntimeException("Control Statement: " + ctx.text + " has not been implemented yet.")
    }

}
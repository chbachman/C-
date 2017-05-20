package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.control.Control
import com.chbachman.cminus.representation.statement.*
import com.chbachman.cminus.representation.value.*

/**
 * Created by Chandler on 5/17/17.
 */
object Parser {

    @JvmStatic
    fun parse(ctx: CMinusParser.ValueContext, scope: Scope): Value {
        // Regular, non struct variable.
        if (ctx.ID() != null) {
            val name = ctx.ID().text
            val v = scope.getVariable(name)

            v ?: throw RuntimeException("Invalid Variable: $name")

            if (ctx.dot() != null) {
                return parse(ctx.dot(), v, scope)
            }

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
    fun parse(ctx: CMinusParser.DotContext, parent: Variable, scope: Scope): Value {
        if (ctx.functionCall() != null) {
            return FunctionCall(ctx.functionCall(), scope)
        }

        // ITT: Lots of Optional Unwrapping.
        if (ctx.ID() != null) {
            val name = ctx.ID().text

            parent.value ?: throw RuntimeException("Should never happen, but the struct variable doesn't have a value.")

            val structCon = parent.value as? StructConstructor ?: throw RuntimeException("Should never happen, but the struct variable isn't a struct.")

            val current = structCon.struct.getVariable(name) ?: throw RuntimeException("Struct ${parent.type} ${parent.name} does not contain the variable $name")

            // Recursive dot parsing. If we have a dot, create the variable and move on.
            if (ctx.dot() != null) {
                val v = parse(ctx.dot(), current, scope)
                return Variable("${parent.name}.${v.value()}", v)
            }

            if (current.value != null) {
                return Variable("${parent.name}.${current.name}", current.value)
            }

            return Variable("${parent.name}.${current.name}", current.type)
        }

        throw RuntimeException("Type of dot expression isn't implemented. " + ctx.text)
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
            return Control.parse(ctx.control(), scope)
        }

        throw RuntimeException("The statement type: " + ctx.text + " is not implemented yet.")
    }

    @JvmStatic
    fun parse(ctx: CMinusParser.CodeBlockContext, scope: Scope): List<Statement> {
        return ctx.statement().map { Parser.parse(it, scope) }
    }

    @JvmStatic
    fun parse(ctx: CMinusParser.FunctionCallContext, scope: Scope): FunctionCall {
        val name = ctx.ID().text
        val struct = scope.getStruct(name)
        val func = scope.functionExists(name)

        if (func && struct != null) {
            throw RuntimeException("There is both a struct and function named: $name")
        }

        if (func) {
            return FunctionCall(ctx, scope)
        }

        if (struct != null) {
            return StructConstructor(ctx, scope)
        }

        throw RuntimeException("The function type: ${ctx.text} is not implemented yet.")
    }

}
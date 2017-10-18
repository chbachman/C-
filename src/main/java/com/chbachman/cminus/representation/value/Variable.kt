package com.chbachman.cminus.representation.value

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type

/**
 * Created by Chandler on 4/12/17.
 * Represents a variable, either a new one or an assignment?
 * TODO: Break this up into assignment and new.
 */
class Variable private constructor(
        val name: Identifier,
        private val value: Expression?,
        override val type: Type
) : Expression, Statement {

    constructor(ctx: CMinusParser.VariableContext, scope: Scope):
            this(Identifier(ctx.ID().text), makeValue(ctx, scope), makeType(ctx, scope))

    constructor(ctx: CMinusParser.ParameterContext):
            this(Identifier(ctx.ID().text), Type[ctx.type()])

    companion object {
        fun makeValue(ctx: CMinusParser.VariableContext, scope: Scope): Expression? {
            return ctx.value()?.let { Parser.parse(it, scope) }
        }

        fun makeType(ctx: CMinusParser.VariableContext, scope: Scope): Type {
            val type = ctx.type()?.let { Type[it] }
            val value = makeValue(ctx, scope)

            return value?.type ?: (type ?: throw IllegalStateException("Variable has no type or value"))
        }
    }

    // Two main constructors
    // constructor(name: Identifier, value: Expression): this(name, value, value.type)
    constructor(name: Identifier, type: Type): this(name, null, type)

    // The declaration of the variable.
    override val statement: String
        get() {
            if (value != null) {
                return "${type.code()} $name = ${value.expression};"
            } else {
                return "${type.code()} $name;"
            }
        }

    private var init = value != null

    fun init() {
        init = true
    }

    val initalized: Boolean
        get() = init


    operator fun plus(other: Variable): Variable {
        return Variable(name + other.name, other.value, other.type)
    }

    // The use of the variable.
    override val expression = name.toString()
}

class ContainerVariable()

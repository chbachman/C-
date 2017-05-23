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
        val name: String,
        val value: Expression?,
        override val type: Type,
        val new: Boolean = true
) : Expression, Statement {

    constructor(ctx: CMinusParser.VariableContext, scope: Scope):
            this(ctx.ID().text, makeValue(ctx, scope), makeType(ctx, scope), ctx.`var` != null) {
        if (new) {
            scope.addVariable(this)
        }
    }

    companion object {
        fun makeValue(ctx: CMinusParser.VariableContext, scope: Scope): Expression? {
            return ctx.value()?.let { Parser.parse(it, scope) }
        }

        fun makeType(ctx: CMinusParser.VariableContext, scope: Scope): Type {
            val type = ctx.type()?.let { Type.from(it) }
            val value = makeValue(ctx, scope)

            return value?.type ?: (type ?: throw IllegalStateException("Variable has no type or value"))
        }
    }

    // Two main constructors
    constructor(name: String, value: Expression): this(name, value, value.type)
    constructor(name: String, type: Type): this(name, null, type)

    constructor(ctx: CMinusParser.ParameterContext): this(ctx.ID().text, Type.from(ctx.type()))
    constructor(parent: Variable, child: Variable): this("${parent.name}.${child.name}", child.value, child.type)

    // The declaration of the variable.
    override val statement: String
        get() {
            if (value != null) {
                var code = ""
                // Add the Type to make it a new variable
                if (new) {
                    code = value.type.code() + " "
                }

                return code + name + " = " + value.expression + ";"
            } else {
                return type.code() + " " + name + ";"
            }
        }

    // The use of the variable.
    override val expression = name
}

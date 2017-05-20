package com.chbachman.cminus.representation.value

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.statement.Statement

import java.util.Optional

/**
 * Created by Chandler on 4/12/17.
 */
class Variable private constructor(
        name: String,
        value: Value?,
        type: Type
) : Value, Statement {

    val name = name
    val value = value
    override val type = type
    private var newVariable = true

    constructor(ctx: CMinusParser.VariableContext, scope: Scope):
            this(ctx.ID().text, Parser.parse(ctx.value(), scope), Type.from(ctx.type())) {

        newVariable = ctx.`var` != null
    }

    // Two main constructors
    constructor(name: String, value: Value): this(name, value, value.type)
    constructor(name: String, type: Type): this(name, null, type)

    constructor(ctx: CMinusParser.ParameterContext): this(ctx.ID().text, Type.from(ctx.type()))
    constructor(parent: Variable, child: Variable): this("${parent.name}.${child.name}", child.value, child.type)

    override fun code(): String {
        if (value != null) {
            var code = ""
            // Add the Type to make it a new variable
            if (newVariable) {
                code = value.type.code() + " "
            }

            return code + name + " = " + value.value() + ";"
        } else {
            return type.code() + " " + name + ";"
        }
    }

    override fun value(): String {
        return name
    }
}

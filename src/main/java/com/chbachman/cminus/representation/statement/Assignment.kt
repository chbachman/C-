package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.value.Value
import com.chbachman.cminus.representation.value.Variable

import java.util.Optional

/**
 * Created by Chandler on 4/16/17.
 */
class Assignment(name: String, value: Value) : Value, Statement {

    override val type = value.type
    val name = name
    val value = value

    constructor(ctx: CMinusParser.AssignmentContext, scope: Scope): this(
            scope.getVariable(ctx.ID().text)?.name
                    ?: throw RuntimeException("Could not find variable: ${ctx.ID().text}"),
            Parser.parse(ctx.value(), scope))
    
    override fun code(): String {
        return name + " = " + value.value() + ";"
    }

    override fun value(): String {
        return name + " = " + value.value()
    }
}

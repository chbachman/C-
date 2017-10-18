package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.value.Expression
import com.chbachman.cminus.representation.value.Identifier
import com.chbachman.cminus.representation.value.Statement

/**
 * Created by Chandler on 4/16/17.
 * Represents a existing variable being assigned to.
 */
class Assignment(val name: Identifier, val value: Expression) : Expression, Statement {

    override val type = value.type
    override val statement = name.toString() + " = " + value.expression + ";"
    override val expression = name.toString() + " = " + value.expression

    constructor(ctx: CMinusParser.AssignmentContext, scope: Scope): this(
            scope.getVariable(Identifier(ctx.identifier(), scope))?.name
                    ?: throw RuntimeException("Could not find variable: ${ctx.identifier().text}"),
            Parser.parse(ctx.value(), scope))
}

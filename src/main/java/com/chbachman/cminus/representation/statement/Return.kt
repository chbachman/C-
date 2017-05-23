package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.value.Expression
import com.chbachman.cminus.representation.value.Statement

/**
 * Created by Chandler on 4/12/17.
 * Handles return values for a function.
 */
class Return(private val value: Expression) : Statement {

    constructor(ctx: CMinusParser.RetContext, scope: Scope): this(Parser.parse(ctx.value(), scope))

    override val statement: String
        get() = "return " + value.expression + ";"
}

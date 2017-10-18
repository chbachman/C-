package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.function.Header
import com.chbachman.cminus.representation.value.Expression
import com.chbachman.cminus.representation.value.Identifier
import com.chbachman.cminus.representation.value.Statement
import com.chbachman.cminus.representation.get

/**
 * Created by Chandler on 4/12/17.
 * Represents a function call.
 */

interface Call: Expression, Statement {
    val ref: Header
    val parameters: List<Expression>
    val name: Identifier

    fun verify() {
        if (parameters.size != ref.parameters.size) {
            throw RuntimeException("Function $name does not have ${parameters.size} parameters.")
        }

        for (i in 0..parameters.size - 1) {
            if (parameters[i].type !== ref.parameters[i].type) {
                throw RuntimeException("Function $name does not have ${parameters[i].type.code()} as a parameter")
            }
        }
    }

    override val type: Type
        get() = ref.type

    override val statement: String
        get() = expression + ';'

    override val expression: String
        get() {
            val s = StringBuilder(ref.cName)
                    .append('(')

            for (value in parameters) {
                s.append(value.expression).append(", ")
            }

            if (!parameters.isEmpty()) {
                s.delete(s.length - 2, s.length)
            }

            s.append(')')

            return s.toString()
        }
}

class FunctionCall(ctx: CMinusParser.FunctionCallContext, scope: Scope): Call {
    override val name = Identifier(ctx, scope)
    override val parameters: List<Expression>
    override val ref: Header

    init {
        val func = name.last as Identifier.FuncSegment
        parameters = func.parameters
        ref = func.ref
        verify()
    }
}
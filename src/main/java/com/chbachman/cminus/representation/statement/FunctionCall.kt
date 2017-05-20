package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.function.ParameterList
import com.chbachman.cminus.representation.value.Value

/**
 * Created by Chandler on 4/12/17.
 */
open class FunctionCall(ctx: CMinusParser.FunctionCallContext, scope: Scope): Value, Statement {
    internal val name = ctx.ID().text
    internal var parameters = ParameterList.parse(ctx.argumentList(), scope)
    override val type: Type
        get() = ref.type

    // If we don't have a ref defined, then get one from name.
    // Otherwise pull from the defined ref.
    private var _ref: Function? = null
    internal val ref: Function by lazy {
        if (_ref != null) {
            return@lazy _ref!!
        }

        scope.getFunction(name, parameters) ?: throw RuntimeException("Function $name was not found.")
    }

    protected constructor(ctx: CMinusParser.FunctionCallContext, scope: Scope, ref: Function) : this(ctx, scope) {
        // Setup custom ref, instead of computed ref.
        this._ref = ref

        if (parameters.size != ref.parameters.size) {
            throw RuntimeException("Function $name does not have ${parameters.size} parameters.")
        }

        for (i in 0..parameters.size - 1) {
            if (parameters[i].type !== ref.parameters[i].type) {
                throw RuntimeException("Function $name does not have ${parameters[i].type.code()} as a parameter")
            }
        }
    }

    override fun code(): String {
        return value() + ';'
    }

    override fun value(): String {
        val s = StringBuilder(ref.cName)
                .append('(')

        for (value in parameters) {
            s.append(value.value()).append(", ")
        }

        if (!parameters.isEmpty()) {
            s.delete(s.length - 2, s.length)
        }

        s.append(')')

        return s.toString()
    }
}

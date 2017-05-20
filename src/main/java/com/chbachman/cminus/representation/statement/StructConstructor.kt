package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Struct
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.function.ParameterList

/**
 * Created by Chandler on 4/23/17.
 */
class StructConstructor(ctx: CMinusParser.FunctionCallContext, scope: Scope) : FunctionCall(ctx, scope, makeFunction(ctx, scope)) {
    val struct = scope.getStruct(ctx.ID().text) ?: throw RuntimeException("Could not find function")

    companion object {
        private fun makeFunction(ctx: CMinusParser.FunctionCallContext, scope: Scope): Function {
            // TODO: Fix this
            val name = ctx.ID().text
            val struct = scope.getStruct(name) ?: throw RuntimeException("Function $name was not found.")
            val parameters = ParameterList.parse(ctx.argumentList(), scope)
            val init = struct.getInit(name, parameters) ?: throw RuntimeException("Parameters didn't match")

            return init
        }
    }


}

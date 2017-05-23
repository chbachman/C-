package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.struct.InitHeader

/**
 * Created by Chandler on 4/23/17.
 */
class StructConstructor(ctx: CMinusParser.FunctionCallContext, scope: Scope) : FunctionCall(ctx, scope, makeFunction(ctx, scope)) {
    val struct = scope.structs[ctx.ID().text]?.getStruct(scope) ?: throw RuntimeException("Could not find function")

    companion object {
        private fun makeFunction(ctx: CMinusParser.FunctionCallContext, scope: Scope): InitHeader {
            // TODO: Fix this
            val name = ctx.ID().text
            val struct = scope.structs[name] ?: throw RuntimeException("Function $name was not found.")
            val parameters = Parser.parse(ctx.argumentList(), scope)
            val init = struct.inits.find { it.matches("init" + name, parameters) } ?: throw RuntimeException("Parameters didn't match")

            return init
        }
    }


}

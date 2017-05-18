package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.CMinusParser
import com.chbachman.cminus.representation.InitBlock
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Struct
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.function.ParameterList
import com.chbachman.cminus.representation.value.Value
import com.chbachman.cminus.representation.value.Variable
import java.util.Optional
import java.util.stream.Collectors

/**
 * Created by Chandler on 4/23/17.
 */
class StructConstructor(ctx: CMinusParser.FunctionCallContext, scope: Scope) : FunctionCall(ctx, scope, makeFunction(ctx, scope)) {
    val struct: Struct

    init {
        // Since we already checked all this in the 'makeFunction' call, we can just directly access it.
        this.struct = scope.getStruct(ctx.ID().text) ?: throw RuntimeException("Could not find function")
    }

    companion object {
        private fun makeFunction(ctx: CMinusParser.FunctionCallContext, scope: Scope): Function {
            // TODO: Fix this
            val name = ctx.ID().text
            val struct = scope.getStruct(name)
            val parameters = ParameterList.parse(ctx.argumentList(), scope)


            if (struct == null) {
                throw RuntimeException("Function $name was not found.")
            }

            val init = struct.getInit(name, parameters)

            if (!init.isPresent) {
                throw RuntimeException("Parameters didn't match")
            }

            return init.get()
        }
    }


}

package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.value.Identifier

/**
 * Created by Chandler on 4/23/17.
 * Handles the construction of Structs
 */
class StructConstructor(ctx: CMinusParser.FunctionCallContext, scope: Scope) : Call {
    override val name = Identifier(ctx, scope)
    override val parameters = Parser.parse(ctx.funcSegment().argumentList(), scope)
    val struct = Type.getStruct(name.text)?.getStruct(scope)
            ?: throw RuntimeException("Could not find function")

    override val ref = struct.inits.map { it.header }.find { it.matches("init" + name, parameters) }
            ?: throw RuntimeException("Parameters didn't match")

    init {
        verify()
    }
}


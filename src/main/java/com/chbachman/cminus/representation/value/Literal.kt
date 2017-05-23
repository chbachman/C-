package com.chbachman.cminus.representation.value

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Type

/**
 * Created by Chandler on 4/12/17.
 * Represents a literal value inside source code.
 */
class Literal(ctx: CMinusParser.LiteralContext) : Value {

    override val type: Type
    private var code: String

    init {
        if (ctx.INT() != null) {
            type = Type.Native.INT.type
            code = ctx.text
        } else if (ctx.STRING() != null) {
            type = Type.Native.STRING.type
            code = ctx.text
        } else if (ctx.BOOL() != null) {
            type = Type.Native.BOOL.type
            code = if (ctx.text.trim { it <= ' ' } == "true") "1" else "0"
        } else {
            throw RuntimeException("Type of Literal ${ctx.text} is not implemented yet.")
        }
    }

    override fun value(): String {
        return code
    }
}

package com.chbachman.cminus.representation.value

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Type

/**
 * Created by Chandler on 4/12/17.
 * Represents a literal value inside source code.
 */
class Literal(ctx: CMinusParser.LiteralContext) : Expression {

    override val type: Type
    override val expression: String

    init {
        if (ctx.INT() != null) {
            type = Type.Native.INT.type
            expression = ctx.text
        } else if (ctx.STRING() != null) {
            type = Type.Native.STRING.type
            expression = ctx.text
        } else if (ctx.BOOL() != null) {
            type = Type.Native.BOOL.type
            expression = if (ctx.text.trim { it <= ' ' } == "true") "1" else "0"
        } else {
            throw RuntimeException("Type of Literal ${ctx.text} is not implemented yet.")
        }
    }


}

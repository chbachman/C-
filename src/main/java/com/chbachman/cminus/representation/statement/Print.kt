package com.chbachman.cminus.representation.statement

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.value.Statement

/**
 * Created by Chandler on 4/12/17.
 * Deals with Print statements and modifiers.
 */
class Print(ctx: CMinusParser.PrintContext, scope: Scope) : Statement {

    val value = Parser.parse(ctx.value(), scope)
    val modifier: String

    init {
        when (value.type) {
            Type.Native.INT.type -> modifier = "%d"
            Type.Native.STRING.type -> modifier = "%s"
            Type.Native.BOOL.type -> modifier = "%s"
            else -> throw RuntimeException("Cannot print value " + value.expression)
        }
    }

    override val statement: String
        get() {
            if (value.type == Type.Native.BOOL.type) {
                return "printf(\"$modifier\\n\", ${value.expression} ? \"true\" : \"false\");"
            }
            return "printf(\"$modifier\\n\", ${value.expression});"
        }
}

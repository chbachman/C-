package com.chbachman.cminus.representation.function

import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.Typed

/**
 * Created by Chandler on 4/9/17.
 * Representation of Function.
 */
abstract class FunctionType : CodeBlockHolder(), Typed {

    abstract val header: Header
    override val type: Type
            get() = header.type

    override fun setupScope(scope: Scope) {
        for (v in header.parameters) {
            scope.addVariable(v)
        }
    }

    override val first: String
        get() {
            val b = StringBuilder().append(header.header)
            // Get rid of semicolon, add {
            b.deleteCharAt(b.length - 1).append(" {")

            return b.toString()
        }

    override val last = "}\n"
}
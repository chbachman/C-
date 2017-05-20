package com.chbachman.cminus.representation.function

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.statement.Statement

/**
 * Created by Chandler on 5/2/17.
 */
abstract class CodeBlockHolder : CodeBlock {

    abstract protected val statements: List<Statement>

    override val middle: String
        get() {
            val b = StringBuilder()

            for (s in statements) {
                b.append(s.code())
                b.append('\n')
            }

            return b.toString()
        }
}

package com.chbachman.cminus.representation.function

import com.chbachman.cminus.representation.value.Statement

/**
 * Created by Chandler on 5/2/17.
 * Default Implementation of CodeBlock, handles Middle
 */
abstract class CodeBlockHolder : CodeBlock {

    abstract protected val statements: List<Statement>

    override val middle: String
        get() {
            val b = StringBuilder()

            for (s in statements) {
                b.append(s.statement)
                b.append('\n')
            }

            return b.toString()
        }
}

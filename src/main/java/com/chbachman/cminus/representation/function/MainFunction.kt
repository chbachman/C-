package com.chbachman.cminus.representation.function

import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.statement.Statement
import com.chbachman.cminus.representation.value.Variable

/**
 * Created by Chandler on 4/15/17.
 * Handles the creation of the main function with the default template.
 */
class MainFunction: FunctionType() {
    override val header = MainFunctionHeader()
    override val last = "    return 0;\n}"
    override val statements = emptyList<Statement>()
}

class MainFunctionHeader: Header() {
    override val type = Type.Native.INT.type
    override val baseName = "main"
    override val parameters = listOf<Variable>()

    override fun getFunc(scope: Scope): MainFunction {
        return MainFunction()
    }
}

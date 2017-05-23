package com.chbachman.cminus.representation.function

import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.value.Statement

/**
 * Created by Chandler on 4/13/17.
 * Handles the creation of new scopes, and deals with variables getting setup.
 */
interface CodeBlock : Statement {
    val first: String
    val middle: String
    val last: String

    fun setupScope(scope: Scope) {}

    override val statement: String
        get() = "$first$middle$last"
}

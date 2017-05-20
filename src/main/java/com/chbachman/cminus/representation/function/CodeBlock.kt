package com.chbachman.cminus.representation.function

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.statement.Statement

import java.util.Collections
import java.util.stream.Collectors

/**
 * Created by Chandler on 4/13/17.
 */
interface CodeBlock : Statement {
    val first: String
    val middle: String
    val last: String

    fun setupScope(scope: Scope) {}

    override fun code(): String {
        return "$first$middle$last"
    }
}

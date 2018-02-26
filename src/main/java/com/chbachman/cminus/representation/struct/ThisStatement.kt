package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Expression

class ThisStatement(ctx: Kotlin.ThisExpressionContext): Expression {
    override val type = SymbolTable.thisType ?: throw RuntimeException("Cannot find this type.")

    override fun toString(): String {
        return "this"
    }
}
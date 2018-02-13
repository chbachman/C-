package com.chbachman.cminus.representation

import com.chbachman.cminus.NameTable
import com.chbachman.cminus.gen.Kotlin

class Property(ctx: Kotlin.PropertyDeclarationContext): Statement {
    val exp = Parser.parse(ctx.expression())
    val name = ctx.variableDeclaration().simpleIdentifier().text

    init {
        NameTable.addVariable(name, exp.type)
    }

    override fun toString(): String {
        return "${exp.type} $name = $exp"
    }
}
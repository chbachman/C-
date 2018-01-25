package com.chbachman.cminus.representation

import com.chbachman.cminus.ScopeStack
import com.chbachman.cminus.gen.Kotlin

class Property(ctx: Kotlin.PropertyDeclarationContext): Statement {
    val exp = Parser.parse(ctx.expression())
    val name = ctx.variableDeclaration().simpleIdentifier().text

    init {
        ScopeStack.addVariable(name, exp.type)
    }

    override fun toString(): String {
        return "${exp.type} $name = $exp"
    }
}
package com.chbachman.cminus.representation

import com.chbachman.cminus.ScopeStack
import com.chbachman.cminus.gen.Kotlin

class VariableDecl(val name: String, override val type: Type): Typed {

}

class VariableRef(ctx: Kotlin.SimpleIdentifierContext): Expression {
    val text: String = ctx.text
    override val type = ScopeStack.getVariable(text) ?:
        throw RuntimeException("Variable $text not found")


    override fun toString(): String {
        return text
    }
}
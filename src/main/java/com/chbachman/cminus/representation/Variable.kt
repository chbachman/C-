package com.chbachman.cminus.representation

import com.chbachman.cminus.NameTable
import com.chbachman.cminus.gen.Kotlin

class VariableDecl(
    val name: String,
    val assignment: Expression
): Expression {

    override val type: Type
        get() = assignment.type

    override fun toString(): String {
        return "$type $name = $assignment"
    }
}

class VariableRef(ctx: Kotlin.SimpleIdentifierContext): Expression {
    val name: String
    override val type: Type

    init {
        val text = ctx.text
        val pair = NameTable.getVariable(ctx.text) ?: throw RuntimeException("Variable $text not found")
        name = pair.first
        type = pair.second
    }


    override fun toString(): String {
        return name
    }
}
package com.chbachman.cminus.representation

import com.chbachman.cminus.Namespace
import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.Variable
import com.chbachman.cminus.gen.Kotlin

class VariableDecl(val name: String, val assignment: Expression): Expression {
    override val type: Type
        get() = assignment.type

    fun addToNameTable(): VariableDecl {
        SymbolTable.addVariable(name, type)
        return this
    }

    override fun toString(): String {
        return "$type $name = $assignment"
    }
}

class VariableRef(ctx: Kotlin.SimpleIdentifierContext, table: Namespace = SymbolTable): Expression, Variable {
    val variable: Variable

    override val name: String
        get() = variable.name

    override val type: Type
        get() = variable.type

    init {
        val text = ctx.text
        variable = table[ctx.text] ?: throw RuntimeException("Variable $text not found")
    }

    override fun get(): String {
        return variable.get()
    }

    override fun set(): String {
        return variable.set()
    }

    override fun toString(): String {
        return variable.toString()
    }
}

abstract class VariableMod(val variable: Variable): Variable {
    override val name: String
        get() = variable.name
    override val type: Type
        get() = variable.type

    override fun toString(): String {
        return get()
    }
}
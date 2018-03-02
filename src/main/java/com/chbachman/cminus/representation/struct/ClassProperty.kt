package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.Namespace
import com.chbachman.cminus.Variable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Type

class ClassProperty(ctx: Kotlin.PropertyDeclarationContext, val parent: Type): Variable {
    val expression: Expression?
    override val type: Type
    override val name: String = ctx.variableDeclaration().simpleIdentifier().text
    var initalized: Boolean

    init {
        val (type, expression) = getType(ctx.expression(), ctx.variableDeclaration().type())
        this.type = type
        this.expression = expression
        initalized = expression != null
    }

    private fun getType(e: Kotlin.ExpressionContext?, t: Kotlin.TypeContext?): Pair<Type, Expression?> {
        val exp = if (e != null) { Parser.parse(e) } else { null }
        val type = if (t != null) { Type[t] } else { null }

        return if (type != null && exp != null) {
            if (type != exp.type) {
                throw RuntimeException("$type and $exp are not the same type.")
            }

            Pair(type, exp)
        } else if (type != null) {
            Pair(type, null)
        } else if (exp != null) {
            Pair(exp.type, exp)
        } else {
            throw RuntimeException("Variable $name does not have a type.")
        }
    }

    fun addToNameTable(ns: Namespace): ClassProperty {
        ns.addVariable(name, type)
        return this
    }

    override fun set(): String {
        initalized = true
        return name
    }

    override fun get(): String {
        return name
    }

    fun initialization(variable: String): Expression? {
        return if (expression != null) { ClassPropertyInit(variable) } else { null }
    }

    override fun toString(): String {
        return "$type $name;"
    }

    private inner class ClassPropertyInit(val variable: String): Expression {
        override val type: Type
            get() = this@ClassProperty.type

        override fun toString(): String {
            return "$variable->$name = $expression"
        }
    }
}


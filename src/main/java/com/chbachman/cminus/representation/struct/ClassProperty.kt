package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Type

class ClassProperty(ctx: Kotlin.PropertyDeclarationContext) {
    val expression = Parser.parse(ctx.expression())
    val type = expression.type
    val identifier: String = ctx.variableDeclaration().simpleIdentifier().text

    fun initialization(variable: String): Expression {
        return ClassPropertyInit(variable)
    }

    override fun toString(): String {
        return "$type $identifier;"
    }

    private inner class ClassPropertyInit(val variable: String): Expression {
        override val type: Type
            get() = this@ClassProperty.type

        override fun toString(): String {
            return "$variable->$identifier = $expression"
        }
    }
}


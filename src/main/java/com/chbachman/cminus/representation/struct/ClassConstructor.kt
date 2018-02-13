package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.NameTable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.*
import com.chbachman.cminus.representation.function.CustomFuncHeader
import com.chbachman.cminus.representation.function.Func
import com.chbachman.cminus.static.Constants

class ClassConstructor(
    ctx: Kotlin.ClassDeclarationContext,
    property: List<ClassProperty>,
    type: Type,
    val fullName: String
) : Func() {
    override val header = CustomFuncHeader("String${Constants.NAMESPACE_REPRESENTATION}init", emptyList(), type)

    val code: CodeBlock

    init {
        NameTable.push()
        property.forEach(this::addVariable)

        val thisCreation = VariableDecl("this", Malloc(type))

        NameTable.addVariable(thisCreation.name, thisCreation.type)

        val variableInit = thisCreation + CodeBlock(property.map { it.initialization("this") })

        code = ctx
            .classBody()
            .classMemberDeclaration()
            .mapNotNull { it.anonymousInitializer() }
            .map { Parser.parse(it.block()) }
            .fold(variableInit) { one, two ->
                one + two
            }
            .plus(Return("this"))

        NameTable.pop()
    }

    private fun addVariable(property: ClassProperty) {
        NameTable.addVariable(property.identifier, property.type) { name, type ->
            "this->$name"
        }
    }

    override fun toString(): String {
        return "$header {$code}"
    }

    inner class Malloc(override val type: Type): Expression {

        override fun toString(): String {
            return "($type) malloc(sizeof($fullName))"
        }
    }
}

data class Return(val exp: String): Statement {
    override fun toString(): String {
        return "return $exp"
    }
}
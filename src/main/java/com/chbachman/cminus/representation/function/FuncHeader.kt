package com.chbachman.cminus.representation.function

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Type

abstract class FuncHeader {
    abstract val name: String
    abstract val fullName: String
    abstract val parameters: List<Parameter>
    abstract val returnType: Type

    override fun toString(): String {
        return "$returnType $fullName(" + parameters.joinToString() + ")"
    }
}

class ContextFuncHeader(ctx: Kotlin.FunctionDeclarationContext): FuncHeader() {
    override val name: String
    override val fullName: String
    override val parameters: List<Parameter>
    override val returnType: Type

    init {
        val shortName = ctx.identifier().simpleIdentifier().first().Identifier().text

        name = shortName

        parameters = ctx.functionValueParameters().functionValueParameter().map {
            val parameter = it.parameter()
            Parameter(
                Type[parameter.type()]
                    ?: throw RuntimeException("Type ${parameter.type().text} does not exist."),
                parameter.simpleIdentifier().text
            )
        }

        val other = SymbolTable[name, parameters.map { it.type }].firstOrNull()

        if (other != null) {
            fullName = other.fullName
            returnType = other.returnType
        } else {
            // C doesn't support method overloading, so to add it we try a simple name and if it doesn't work
            // Then we move to a complicated name with $
            fullName = if (SymbolTable.hasFuncWithName(name)) {
                name + "$" + parameters.joinToString("$") { it.type.typeName }
            } else {
                name
            }

            returnType = if (ctx.returnType != null) {
                Type[ctx.returnType] ?: Type.Native.Unit
            } else {
                Type.Native.Unit
            }
        }
    }
}

class CustomFuncHeader(
    override val name: String,
    override val parameters: List<Parameter>,
    override val returnType: Type,
    override val fullName: String = name
): FuncHeader()
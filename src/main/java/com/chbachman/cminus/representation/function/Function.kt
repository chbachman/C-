package com.chbachman.cminus.representation.function

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.CodeBlock
import com.chbachman.cminus.representation.TopLevel
import com.chbachman.cminus.representation.Type

data class Parameter(val type: Type, val name: String) {
    override fun toString(): String {
        return type.toString() + " " + name
    }
}

abstract class Function: TopLevel {
    abstract val header: Header<Function>

    val name: String
        get() = header.name

    val parameters: List<Parameter>
        get() = header.parameters

    val returnType: Type
        get() = header.returnType

    abstract val block: CodeBlock

    val pointer: String
        get() {
            return "$returnType (*$name)(${parameters.joinToString()})"
        }
}

typealias FunctionHeader = Header<Function>
abstract class Header<out T: Function> {
    abstract val name: String
    abstract val fullName: String
    abstract val parameters: List<Parameter>
    abstract val returnType: Type
    open val inline = false

    override fun toString(): String {
        return "$returnType $fullName(" + parameters.joinToString() + ")"
    }

    abstract fun parse(): T
}

abstract class ContextFunctionHeader<out T: Function>(val ctx: Kotlin.FunctionDeclarationContext): Header<T>() {
    final override val name: String
    final override val fullName: String
    final override val parameters: List<Parameter>
    final override val returnType: Type
    final override val inline: Boolean

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

        inline = ctx.modifierList()?.modifier()?.any { it.functionModifier()?.INLINE() != null } ?: false

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
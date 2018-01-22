package com.chbachman.cminus.representation

import com.chbachman.cminus.ScopeStack
import com.chbachman.cminus.gen.Kotlin

class FuncHeader(ctx: Kotlin.FunctionDeclarationContext) {
    val name = ctx.identifier().simpleIdentifier().first().Identifier().text
    val fullName: String
    val parameters: List<Parameter>
    val returnType: Type

    init {
        parameters = ctx.functionValueParameters().functionValueParameter().map {
            val parameter = it.parameter()
            Parameter(
                Type[parameter.type()] ?: throw RuntimeException("Type ${parameter.type().text} does not exist."),
                parameter.simpleIdentifier().text
            )
        }

        val other = ScopeStack.getFunc(name, parameters.map { it.type })

        if (other != null) {
            fullName = other.fullName
            returnType = other.returnType
        } else {
            // C doesn't support method overloading, so to add it we try a simple name and if it doesn't work
            // Then we move to a complicated name with $
            fullName = if (ScopeStack.hasFuncWithName(name)) {
                name + parametersToString()
            } else {
                name
            }

            returnType = if (ctx.returnType != null) {
                Type[ctx.returnType.text] ?: Type.Native.VOID.type
            } else {
                Type.Native.VOID.type
            }
        }
    }

    private fun parametersToString(): String {
        return "$" + parameters.map { it.type.typeName }.reduce { s1, s2 ->
            "$s1\$$s2"
        }
    }

    override fun toString(): String {
        return "$returnType $fullName(" + parameters.map { it.toString() }.reduce { s1, s2 ->
            s1 + ", " + s2
        } + ")"
    }
}

class Func(ctx: Kotlin.FunctionDeclarationContext): TopLevel {
    private val header = FuncHeader(ctx)
    val name = header.name
    val parameters = header.parameters
    val returnType = header.returnType
    val block: CodeBlock

    init {
        ScopeStack.push()
        parameters.forEach { ScopeStack.addVariable(it.name, it.type) }
        block = Parser.parse(ctx.functionBody().block())
        ScopeStack.pop()
    }

    override fun toString(): String {
        return "$header { $block } "
    }

}

data class Parameter(val type: Type, val name: String) {
    override fun toString(): String {
        return type.toString() + " " + name
    }
}
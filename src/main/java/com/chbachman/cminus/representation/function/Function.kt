package com.chbachman.cminus.representation.function

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.CodeBlock
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.TopLevel
import com.chbachman.cminus.representation.Type

abstract class Func: TopLevel {
    abstract val header: FuncHeader

    val name: String
        get() = header.name

    val parameters: List<Parameter>
        get() = header.parameters

    val returnType: Type
        get() = header.returnType

    val pointer: String
        get() {
            return "$returnType (*$name)(${parameters.joinToString()})"
        }
}

class NativeFunc(ctx: Kotlin.FunctionDeclarationContext): Func() {
    override val header = ContextFuncHeader(ctx)

    override fun toString(): String {
        return ""
    }
}

class DeclaredFunc(ctx: Kotlin.FunctionDeclarationContext): Func() {
    override val header = ContextFuncHeader(ctx)
    val block: CodeBlock

    init {
        SymbolTable.push()
        parameters.forEach { SymbolTable.addVariable(it.name, it.type) }
        block = Parser.parse(ctx.functionBody().block())
        SymbolTable.pop()
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
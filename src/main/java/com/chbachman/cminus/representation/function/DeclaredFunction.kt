package com.chbachman.cminus.representation.function

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.CodeBlock
import com.chbachman.cminus.representation.Parser

class DeclaredFunctionHeader(ctx: Kotlin.FunctionDeclarationContext): ContextFunctionHeader<DeclaredFunction>(ctx) {
    override fun parse(): DeclaredFunction {
        return DeclaredFunction(ctx,this)
    }
}

class DeclaredFunction(
    ctx: Kotlin.FunctionDeclarationContext,
    override val header: DeclaredFunctionHeader
): Function() {
    override val block: CodeBlock

    constructor(ctx: Kotlin.FunctionDeclarationContext): this(ctx, DeclaredFunctionHeader(ctx))

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
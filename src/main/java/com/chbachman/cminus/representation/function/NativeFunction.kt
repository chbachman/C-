package com.chbachman.cminus.representation.function

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.CodeBlock

class NativeFunctionHeader(ctx: Kotlin.FunctionDeclarationContext): ContextFunctionHeader<NativeFunction>(ctx) {
    override fun parse(): NativeFunction {
        return NativeFunction(ctx)
    }
}

class NativeFunction(ctx: Kotlin.FunctionDeclarationContext): Function() {
    override val header = NativeFunctionHeader(ctx)
    override val block = CodeBlock()

    override fun toString(): String {
        return ""
    }
}
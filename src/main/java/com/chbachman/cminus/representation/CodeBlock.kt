package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin

class CodeBlock(val block: List<Statement>) {

    val type: Type?
        get() = (block.last() as? Expression)?.type

    constructor(ctx: Kotlin.BlockContext): this(ctx.statement().map { Parser.parse(it) })

    operator fun plus(other: CodeBlock): CodeBlock {
        return CodeBlock(this.block + other.block)
    }

    operator fun plus(other: Statement): CodeBlock {
        return CodeBlock(this.block + other)
    }

    override fun toString(): String {
        if (block.isEmpty()) {
            return ""
        }

        return block.joinToString("\n") {
            "$it" + if (it.semicolon) { ";" } else { "" }
        }
    }
}
package com.chbachman.cminus.representation

import com.chbachman.cminus.Block
import com.chbachman.cminus.gen.Kotlin

class CodeBlock(val block: List<Statement>) {

    val type: Type?
        get() = (block.last() as? Expression)?.type

    constructor(ctx: Kotlin.BlockContext): this(parseContext(ctx))

    companion object {
        private fun parseContext(ctx: Kotlin.BlockContext): List<Statement> {
            val statements = mutableListOf<Statement>()

            Block.handler = {
                statements.add(it)
            }

            // Not using stdlib since I don't think order will work.
            for (statement in ctx.statement()) {
                statements.add(Parser.parse(statement))
            }

            return statements
        }
    }

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
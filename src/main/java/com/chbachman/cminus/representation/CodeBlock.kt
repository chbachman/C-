package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin

class CodeBlock(ctx: Kotlin.BlockContext) {
    val block = ctx.statement().map { Parser.parse(it) }

    override fun toString(): String {
        if (block.isEmpty()) {
            return ""
        }

        return block.map {
            "$it" + if (it.semicolon) { ";" } else { "" }
        }.reduce { e1, e2 ->
             "$e1\n$e2"
        }
    }
}
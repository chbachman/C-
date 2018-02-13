package com.chbachman.cminus.representation

interface Typed {
    val type: Type
}

interface Expression: Typed, Statement {}

interface Statement {
    val semicolon: Boolean
        get() = true

    operator fun plus(other: CodeBlock): CodeBlock {
        return CodeBlock(listOf(this) + other.block)
    }
}
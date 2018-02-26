package com.chbachman.cminus.representation.control

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.CodeBlock
import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Parser

class IfExpression(ctx: Kotlin.IfExpressionContext, exp: Boolean): Expression {
    val condition = Parser.parse(ctx.expression())
    val codeBlock: CodeBlock
    override val semicolon = false

    init {
        SymbolTable.push()
        codeBlock = Parser.parse(ctx.controlStructureBody().first().block())
        SymbolTable.pop()
    }

    override val type =
        codeBlock.type ?:
            throw RuntimeException("If Expression used as a expression does not contain expression as last statement.")


    override fun toString(): String {
        return "if ($condition) { $codeBlock }"
    }
}
package com.chbachman.cminus.representation.literal

import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Type
import org.antlr.v4.runtime.tree.TerminalNode

class BooleanLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Boolean
    private val value = ctx.text == "true"

    override fun toString(): String {
        return if (value) "1" else "0"
    }
}

class CharLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Char
    private val value = ctx.text[1]

    override fun toString(): String {
        return "'$value'"
    }
}

class DoubleLiteral(ctx: TerminalNode): Expression {
    override val type: Type
    private val value: Double

    private val forced = ctx.text.last().equals('f', true)
    private val suffix  = if (forced) "F" else ""

    init {
        val text = ctx.text
            .replace("_", "")
            .toLowerCase()
            .removeSuffix("f")

        value = text.toDouble()

        type = if (forced) {
            Type.Native.Float
        } else {
            Type.Native.Double
        }
    }

    override fun toString(): String {
        return value.toString() + suffix
    }
}

class NullLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Nothing

    override fun toString(): String {
        return "NULL"
    }
}

class IntegerLiteral(ctx: TerminalNode): Expression {
    override val type: Type
    private val base: Int
    private val prefix: String
    private val value: Long

    private val forced = ctx.text.last().equals('l', true)
    private val suffix  = if (forced) "L" else ""

    init {
        val text = ctx.text
            .replace("_", "")
            .toLowerCase()
            .removeSuffix("l")

        val pair = when {
            text.startsWith("0x") -> Pair(16, "0x")
            text.startsWith("0b") -> Pair(2, "0b")
            else -> Pair(10, "")
        }

        base = pair.first
        prefix = pair.second

        value = text.removePrefix(prefix).toLong(base)


        type = if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
            Type.Native.Long
        } else {
            if (forced) {
                Type.Native.Long
            } else {
                Type.Native.Int
            }
        }
    }

    override fun toString(): String {
        return prefix + value.toString(base) + suffix
    }
}
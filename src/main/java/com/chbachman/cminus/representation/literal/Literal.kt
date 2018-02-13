package com.chbachman.cminus.representation.literal

import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Type
import org.antlr.v4.runtime.tree.TerminalNode

class BooleanLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Boolean
    val value = ctx.text == "true"

    override fun toString(): String {
        return if (value) "1" else "0"
    }
}

class IntegerLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Int
    val value = ctx.text.toInt()

    override fun toString(): String {
        return value.toString()
    }
}

class HexLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Int
    val value = ctx.text
        .removePrefix("0x")
        .toInt(16)

    override fun toString(): String {
        return "0x" + value.toString(16)
    }
}

class BinLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Int
    val value = ctx.text
        .removePrefix("0b")
        .toInt(2)

    override fun toString(): String {
        return "0b" + value.toString(2)
    }
}

class CharLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Char
    val value = ctx.text[1]

    override fun toString(): String {
        return "'$value'"
    }
}

object RealLiteral {
    fun parse(ctx: TerminalNode): Expression {
        TODO("The value type: " + ctx.text + " is not implemented yet.")
    }
}

class DoubleLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Double
}

class FloatLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Float
}

class NullLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Unit

    override fun toString(): String {
        return "NULL"
    }
}

class LongLiteral(ctx: TerminalNode): Expression {
    override val type = Type.Native.Long
}
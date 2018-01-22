package com.chbachman.cminus.representation.literal

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Type

class StringLiteral(ctx: Kotlin.StringLiteralContext): Expression {
    override val type = Type.Native.CSTRING.type
    private val str = ctx.text

    override fun toString(): String {
        return str
    }
}
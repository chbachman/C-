package com.chbachman.cminus.representation.literal

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Expression
import com.chbachman.cminus.representation.Type

// Will auto-wrap in quotes. The text is the insides of the string literal.
class StringLiteral(text: String): Expression {
    override val type = Type.Native.CString
    val str = "\"$text\""

    constructor(ctx: Kotlin.StringLiteralContext):
        this(ctx.lineStringLiteral().lineStringContent().first().text)

    override fun toString(): String {
        return str
    }
}
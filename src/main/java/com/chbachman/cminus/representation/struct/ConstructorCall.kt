package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.function.FunctionCall
import com.chbachman.cminus.static.Constants

class ConstructorCall(
    override val type: Type,
    ctx: Kotlin.CallExpressionContext
): FunctionCall(ctx) {
    override val fullName = name + Constants.NAMESPACE_REPRESENTATION + "init"
}
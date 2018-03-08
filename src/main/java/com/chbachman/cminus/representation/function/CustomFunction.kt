package com.chbachman.cminus.representation.function

import com.chbachman.cminus.representation.CodeBlock
import com.chbachman.cminus.representation.Type

class CustomFunctionHeader(
    override val name: String,
    val body: String,
    override val fullName: String = name,
    override val parameters: List<Parameter> = emptyList(),
    override val returnType: Type = Type.Native.Unit
) : Header<CustomFunction>() {

    override fun parse(): CustomFunction {
        return CustomFunction(name, body, fullName, parameters, returnType)
    }
}

class CustomFunction(override val header: CustomFunctionHeader): Function() {
    override val block: CodeBlock
        get() = TODO("not implemented")

    constructor(
        name: String,
        body: String,
        fullName: String = name,
        parameters: List<Parameter> = emptyList(),
        returnType: Type = Type.Native.Unit
    ): this(CustomFunctionHeader(name, body, fullName, parameters, returnType))

    override fun toString(): String {
        return "$header { ${header.body} }"
    }
}
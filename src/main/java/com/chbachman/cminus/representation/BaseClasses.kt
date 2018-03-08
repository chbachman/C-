package com.chbachman.cminus.representation

import com.chbachman.cminus.Namespace
import com.chbachman.cminus.Variable
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.function.FunctionHeader

interface Typed {
    val type: Type
}

interface Expression: Typed, Statement

interface Statement {
    val semicolon: Boolean
        get() = true

    operator fun plus(other: CodeBlock): CodeBlock {
        return CodeBlock(listOf(this) + other.block)
    }
}

data class CustomStatement(
    val code: String,
    override val semicolon: Boolean = true
): Statement {
    override fun toString(): String {
        return code
    }
}

abstract class CustomTypeHeader {
    // Full name is the non-typedef'd type name
    // Isn't generally a pointer.
    abstract val fullName: String

    // Name is the typedef'd name
    // Should generally be a pointer
    abstract val name: String

    // The Type for the typedef'd name, which should be used instead of the fullName.
    open val type: Type by lazy {
        Type(name)
    }

    // The methods attached to the name.
    abstract val methods: List<FunctionHeader>
    abstract val properties: List<Variable>

    open fun getNS(): Namespace {
        val ns = Namespace()
        ns += methods
        properties.forEach { ns += it }
        return ns
    }
}

interface CustomType {
    val header: CustomTypeHeader

    val fullName: String
        get() = header.fullName

    val name: String
        get() = header.name

    val type: Type
        get() = header.type

    val methods: List<Function>

    val typedef: String
        get() {
            return "${header.fullName};\n " +
                "typedef ${header.fullName}* ${header.name};\n"
        }
}
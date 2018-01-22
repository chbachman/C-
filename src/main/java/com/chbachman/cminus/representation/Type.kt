package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin

data class Type private constructor(
    val typeName: String,
    private val cType: String = typeName,
    private val reference: Int = 0
) : Typed {

    override val type: Type
        get() = this

    fun reference(): Type {
        return Type(typeName, cType, reference + 1)
    }

    override fun toString(): String {
        return cType + "".padStart(reference, '*')
    }

    companion object {
        private val types = Native.values().map { Pair(it.type.typeName, it.type) }.toMap().toMutableMap()

        operator fun get(index: String): Type? {
            return types[index]
        }

        // TODO: Implement arrays.
        operator fun get(ctx: Kotlin.TypeContext): Type? {
            // TODO: Allow dot notation
            val simple = ctx.typeReference().userType().simpleUserType().first()
            val identifier = simple.simpleIdentifier().text

            // TODO: Implement arrays correctly.
            if (identifier == "CArray") {
                val projection = simple.typeArguments().typeProjection().first().type()
                return Type[projection]?.reference()
            }

            return types[identifier]
        }
    }

    enum class Native constructor(
        val typeName: String,
        val cType: String = typeName,
        private val reference: Int = 0
    ): Typed {
        CSTRING("CString", "char", 1),
        CHAR("Char", "char"),
        INT("Int", "int"),
        LONG("Long", "long int"),
        FLOAT("Float", "float"),
        DOUBLE("Double", "double"),
        VOID("Void", "void"),
        BOOL("Boolean", "int");

        override val type by lazy { Type(typeName, cType, reference) }
    }
}

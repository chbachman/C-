package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin

data class Type private constructor(
    val typeName: String,
    private val cType: String = typeName,
    private val reference: Int = 0,
    private val nullable: Boolean = false
) : Typed {

    override val type: Type
        get() = this

    fun reference(): Type {
        return Type(typeName, cType, reference + 1, nullable)
    }

    fun nullable(): Type {
        return Type(typeName, cType, reference, true)
    }

    fun canConvert(other: Type): Boolean {
        // Null can be passed into every other nullable type.
        if (this == Native.NULL.type) {
            return other.nullable
        }

        // TODO: For right now, only types with the base type can be converted.
        if (other.typeName != this.typeName) {
            return false
        }

        // TODO: For right now, only types with same reference amount can be converted.
        if (other.reference != this.reference) {
            return false;
        }

        if (nullable) {
            return other.nullable
        }

        return true
    }

    override fun toString(): String {
        return cType + "".padStart(reference, '*')
    }

    companion object {
        private val baseTypes = Native.values().map { Pair(it.type.typeName, it.type) }.toMap().toMutableMap()

        operator fun get(ctx: Kotlin.TypeContext): Type? {
            // TODO: Allow dot notation
            val nullable: Boolean
            val simple = if (ctx.nullableType() != null) {
                nullable = true
                ctx.nullableType().typeReference().userType().simpleUserType().first()
            } else {
                nullable = false
                ctx.typeReference().userType().simpleUserType().first()
            }

            // TODO: Allow dot notation
            val identifier = simple.simpleIdentifier().text

            // TODO: Implement arrays correctly.
            if (identifier == "CArray") {
                val projection = simple.typeArguments().typeProjection().first().type()
                return Type[projection]?.reference()
            }

            val finalType = baseTypes[identifier]

            // Since we only get out nullable types, we need to make this one nullable.
            return if (nullable) {
                finalType?.nullable()
            } else {
                finalType
            }
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
        VOID("Unit", "void"),
        BOOL("Boolean", "int"),
        NULL("null", "NULL");

        override val type by lazy { Type(typeName, cType, reference) }
    }
}

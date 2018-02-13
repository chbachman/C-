package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.Kotlin

data class Type constructor(
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
        if (this == Native.Unit) {
            return other.nullable
        }

        // TODO: For right now, only types with the base type can be converted.
        if (other.typeName != this.typeName) {
            return false
        }

        // TODO: For right now, only types with same reference amount can be converted.
        if (other.reference != this.reference) {
            return false
        }

        if (nullable) {
            return other.nullable
        }

        return true
    }

    override fun toString(): String {
        return cType + "".padStart(reference, '*')
    }

    private fun pair(): Pair<String, Type> {
        return Pair(typeName, this)
    }

    companion object {
        private val baseTypes = mutableMapOf(
            Type("CString", "char", 1).pair(),
            Type("Char", "char").pair(),
            Type("Int", "int").pair(),
            Type("Long", "long").pair(),
            Type("Float", "float").pair(),
            Type("Double", "double").pair(),
            Type("Unit", "void").pair(),
            Type("Boolean", "int").pair(),
            Type("null", "NULL").pair()
        )

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

        operator fun get(str: String): Type? {
             return baseTypes[str]
        }

        operator fun plusAssign(type: Type) {
            baseTypes[type.typeName] = type
        }
    }

    object Native {
        val CString = baseTypes["CString"]!!
        val Char = baseTypes["Char"]!!
        val Int = baseTypes["Int"]!!
        val Long = baseTypes["Long"]!!
        val Float = baseTypes["Float"]!!
        val Double = baseTypes["Double"]!!
        val Unit = baseTypes["Unit"]!!
        val Boolean = baseTypes["Boolean"]!!
        val Null = baseTypes["null"]!!
    }
}

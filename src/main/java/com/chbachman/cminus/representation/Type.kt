package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.struct.StructHeader

class Type private constructor(
        val typeName: String,
        val cType: String = typeName,
        val struct: StructHeader? = null
) : Typed {

    override val type: Type
        get() = this

    fun code(): String {
        return cType
    }

    override fun equals(other: Any?): Boolean {
        val otherType = other as? Type ?: return false

        return typeName == otherType.typeName
    }

    override fun hashCode(): Int {
        return typeName.hashCode()
    }

    enum class Native constructor(val typeName: String, val cType: String = typeName): Typed {
        STRING("String", "char *"),
        INT("Int", "int"),
        VOID("Void", "void"),
        BOOL("Bool", "int");

        override val type by lazy { Type(typeName, cType) }
    }

    companion object {
        private val types = Native.values().map { Pair(it.type.typeName, it.type) }.toMap().toMutableMap()

        fun getStruct(index: String): StructHeader? {
            return Type[Type[index] ?: return null]
        }

        operator fun get(index: String): Type? {
            return types[index]
        }

        operator fun get(index: CMinusParser.TypeContext): Type {
            return getType(index.text)
        }

        operator fun get(index: StructHeader): Type {
            return getType(index.name, "struct " + index.name)
        }

        operator fun get(index: Type): StructHeader? {
            return index.struct
        }

        private fun getType(name: String, cType: String = name, struct: StructHeader? = null): Type {
            return types.getOrPut(name, { Type(name, cType, struct) })
        }
    }
}

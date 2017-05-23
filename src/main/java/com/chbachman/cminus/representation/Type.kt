package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.struct.Struct
import com.chbachman.cminus.representation.struct.StructHeader

class Type private constructor(val typeName: String, val cType: String = typeName) : Typed {

    override val type = this

    fun code(): String {
        return cType
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false
        val otherType = other as? Type ?: return false

        return typeName == otherType.typeName
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

        fun from(context: CMinusParser.TypeContext): Type {
            return getType(context.ID().text)
        }

        fun from(struct: StructHeader): Type {
            return getType(struct.name, "struct " + struct.name)
        }

        private fun getType(name: String, cType: String = name): Type {
            return types.getOrPut(name, { Type(name, cType) })
        }
    }
}

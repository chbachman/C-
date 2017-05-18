package com.chbachman.cminus.representation

import com.chbachman.cminus.CMinusParser

import java.util.HashMap

class Type private constructor(val type: String, val cType: String = type) : Typed {

    fun code(): String {
        return cType
    }

    override fun type(): Type {
        return this
    }

    enum class Native constructor(type: String, cType: String = type) {

        STRING("String", "char *"),
        INT("Int", "int"),
        VOID("Void", "void"),
        BOOL("Bool", "int");

        var type = Type(type, cType)

        companion object {
            operator fun get(type: Type): Native {
                return Native.values().find { it.type === type } ?: VOID
            }
        }
    }

    companion object {
        private val types = HashMap<String, Type>()

        fun init() {
            for (n in Native.values()) {
                types.put(n.type.type, n.type)
            }
        }

        fun from(context: CMinusParser.TypeContext): Type {
            return getType(context.ID().text)
        }

        fun from(struct: Struct): Type {
            return getType(struct.name, "struct " + struct.name)
        }

        private fun getType(name: String, cType: String = name): Type {
            if (types.containsKey(name)) {
                return types[name]!!
            } else {
                val t = Type(name, cType)
                types.put(name, t)
                return t
            }
        }
    }
}

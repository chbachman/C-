package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.TopLevel
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.function.DeclaredFunc
import com.chbachman.cminus.util.toPlainString

class ClassDeclaration(ctx: Kotlin.ClassDeclarationContext) : TopLevel {
    val type = getType(ctx)

    private val fullStructName = "struct $type\$truct"

    private val properties = ctx
        .classBody()
        .classMemberDeclaration()
        .mapNotNull { it.propertyDeclaration() }
        .map {
            ClassProperty(it)
        }

    private val functions = ctx
        .classBody()
        .classMemberDeclaration()
        .mapNotNull { it.functionDeclaration() }
        .map {
            DeclaredFunc(it, type)
        }

    private val constructor = ClassConstructor(ctx, properties, type, fullStructName)

    companion object {
        fun getType(ctx: Kotlin.ClassDeclarationContext): Type {
            val name = ctx.simpleIdentifier().text
            return Type(name)
        }
    }

    override fun toString(): String {
        return "$fullStructName { ${properties.toPlainString()} };\n" +
            "typedef $fullStructName* $type;\n" +
            "$constructor\n" +
            "${functions.toPlainString()}\n"
    }
}
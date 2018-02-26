package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.Namespace
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.TopLevel
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.util.toPlainString

class ClassDeclaration(ctx: Kotlin.ClassDeclarationContext) : TopLevel {
    val type = getType(ctx)

    private val fullStructName = "struct $type\$truct"

    private val properties = ctx
        .classBody()
        .classMemberDeclaration()
        .mapNotNull { it.propertyDeclaration() }
        .map {
            ClassProperty(it, type)
        }

    private val functions = ctx
        .classBody()
        .classMemberDeclaration()
        .mapNotNull { it.functionDeclaration() }
        .map {
            ClassFunction(it, type)
        }

    private val constructor = ClassConstructor(ctx, properties, functions, type, fullStructName)

    companion object {
        fun getType(ctx: Kotlin.ClassDeclarationContext): Type {
            val name = ctx.simpleIdentifier().text
            return Type(name)
        }

        fun getNS(ctx: Kotlin.ClassDeclarationContext): Namespace {
            val namespace = Namespace()
            val type = getType(ctx)

            namespace += ctx
                .classBody()
                .classMemberDeclaration()
                .mapNotNull { it.functionDeclaration() }
                .map {
                    ClassFunctionHeader(it, type)
                }

            ctx
                .classBody()
                .classMemberDeclaration()
                .mapNotNull { it.propertyDeclaration() }
                .map {
                    ClassProperty(it, type)
                }
                .forEach { it.addToNameTable(namespace) }

            return namespace
        }
    }

    private fun functionPointers(): String {
        return functions.map { func ->
            "${func.returnType} (*${func.name})(${func.parameters.joinToString()})"
        }.joinToString(";") + ";"
    }

    override fun toString(): String {
        return "$fullStructName;" +
            "typedef $fullStructName* $type;\n" +
            "$fullStructName { " +
                "${properties.toPlainString()} " +
                "${functionPointers()} " +
            "};\n\n" +
            "${functions.toPlainString()}\n\n" +
            "$constructor\n\n"

    }
}
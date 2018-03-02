package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.Namespace
import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.Variable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.*
import com.chbachman.cminus.representation.function.CustomFuncHeader
import com.chbachman.cminus.representation.function.Func
import com.chbachman.cminus.static.Constants
import com.chbachman.cminus.util.toPlainString

class ClassHeader(ctx: Kotlin.ClassDeclarationContext) : CustomTypeHeader() {
    override val name = ctx.simpleIdentifier().text
    override val fullName = "struct $name\$truct"

    override val methods = ctx
        .classBody()
        .classMemberDeclaration()
        .mapNotNull { it.functionDeclaration() }
        .map { ClassFunctionHeader(it, type) }

    override val properties = ctx
        .classBody()
        .classMemberDeclaration()
        .mapNotNull { it.propertyDeclaration() }
        .map { ClassProperty(it, type) }

    override fun getNS(): Namespace {
        val ns = Namespace()
        ns += methods
        properties.forEach { it.addToNameTable(ns) }
        return ns
    }
}

class ClassDeclaration(ctx: Kotlin.ClassDeclarationContext) : CustomType, TopLevel {
    override val header = ClassHeader(ctx)

    override val methods = ctx
        .classBody()
        .classMemberDeclaration()
        .mapNotNull { it.functionDeclaration() }
        .map { ClassFunction(it, type) }

    private val constructor = ClassConstructor(ctx)

    private fun methodPointers(): String {
        return methods.joinToString(";") { it.pointer } + ";"
    }

    override fun toString(): String {
        return typedef +
            "$fullName { " +
                "${header.properties.toPlainString()} " +
                "${methodPointers()} " +
            "};\n\n" +
            "${methods.toPlainString()}\n\n" +
            "$constructor\n\n"
    }

    inner class ClassConstructor(ctx: Kotlin.ClassDeclarationContext) : Func() {
        override val header = CustomFuncHeader(
            "$type${Constants.NAMESPACE_REPRESENTATION}init",
            emptyList(),
            type
        )

        val classVar = "this"
        val code: CodeBlock

        init {
            val code = mutableListOf<Statement>()
            val header = this@ClassDeclaration.header

            SymbolTable.push()
            header.properties.forEach { prop ->
                SymbolTable += VarModifier(prop)
            }

            // Initial Variable Creation
            code += VariableDecl(classVar, Malloc()).addToNameTable()

            // Setup the function pointers.
            code += methods.map { it.header.initialization(classVar) }

            // Setup the property initalizers
            code += header.properties.mapNotNull { it.initialization(classVar) }

            code += ctx
                .classBody()
                .classMemberDeclaration()
                .mapNotNull { it.anonymousInitializer() }
                .flatMap { Parser.parse(it.block()).block }

            code += Return()

            SymbolTable.pop()

            if (!header.properties.all { it.initalized }) {
                throw RuntimeException("Not all class variables created.")
            }

            this.code = CodeBlock(code)
        }

        override fun toString(): String {
            return "$header {$code}"
        }

        // MARK: Classes for creating code with correct typing.
        private inner class Malloc: Expression {
            override val type: Type
                get() = returnType

            override fun toString(): String {
                return "($type) malloc(sizeof($fullName))"
            }
        }

        private inner class Return: Statement {
            override fun toString(): String {
                return "return $classVar"
            }
        }

        private inner class VarModifier(variable: Variable): VariableMod(variable) {
            override fun get(): String {
                return "$classVar->${variable.get()}"
            }

            override fun set(): String {
                return "$classVar->${variable.set()}"
            }
        }
    }
}
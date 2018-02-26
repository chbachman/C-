package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.Variable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.*
import com.chbachman.cminus.representation.function.CustomFuncHeader
import com.chbachman.cminus.representation.function.Func
import com.chbachman.cminus.static.Constants

class ClassConstructor(
    ctx: Kotlin.ClassDeclarationContext,
    property: List<ClassProperty>,
    funcs: List<ClassFunction>,
    type: Type,
    val fullName: String
) : Func() {
    override val header = CustomFuncHeader(
        "String${Constants.NAMESPACE_REPRESENTATION}init",
        emptyList(),
        type
    )

    val classVar = "this"
    val code: CodeBlock

    init {
        val code = mutableListOf<Statement>()

        SymbolTable.push()
        property.forEach { prop ->
            SymbolTable += VarModifier(prop)
        }

        // Initial Variable Creation
        code += VariableDecl(classVar, Malloc()).addToNameTable()

        // Setup the function pointers.
        code += funcs.map { it.header.initialization(classVar) }

        // Setup the property initalizers
        code += property.mapNotNull { it.initialization(classVar) }

        code += ctx
            .classBody()
            .classMemberDeclaration()
            .mapNotNull { it.anonymousInitializer() }
            .flatMap { Parser.parse(it.block()).block }

        code += Return()

        SymbolTable.pop()

        if (!property.all { it.initalized }) {
            throw RuntimeException("Not all class variables created.")
        }

        this.code = CodeBlock(code)
    }

    override fun toString(): String {
        return "$header {$code}"
    }

    inner class Malloc: Expression {
        override val type: Type
            get() = returnType

        override fun toString(): String {
            return "($type) malloc(sizeof($fullName))"
        }
    }

    inner class Return: Statement {
        override fun toString(): String {
            return "return $classVar"
        }
    }

    inner class VarModifier(variable: Variable): VariableMod(variable) {
        override fun get(): String {
            return "$classVar->${variable.get()}"
        }

        override fun set(): String {
            return "$classVar->${variable.set()}"
        }
    }
}
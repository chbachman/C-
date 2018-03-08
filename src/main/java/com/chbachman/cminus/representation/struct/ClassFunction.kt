package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.Variable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.*
import com.chbachman.cminus.representation.function.DeclaredFunctionHeader
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.function.Header
import com.chbachman.cminus.representation.function.Parameter
import com.chbachman.cminus.static.Constants

class ClassFunctionHeader(val ctx: Kotlin.FunctionDeclarationContext, val enclosing: Type): Header<ClassFunction>() {
    override val name: String
    override val fullName: String
    override val parameters: List<Parameter>
    override val returnType: Type

    init {
        // Just to conveniently get most of the details.
        val tempHeader = DeclaredFunctionHeader(ctx)
        val implicitThis = Parameter(enclosing, "this")

        // Setup Qualified Name and FullName
        name = tempHeader.name
        fullName = "$enclosing${Constants.NAMESPACE_REPRESENTATION}${tempHeader.fullName}"

        // Setup parameters with "this"
        parameters = listOf(implicitThis) + tempHeader.parameters

        returnType = tempHeader.returnType
    }

    fun initialization(thisVar: String): Statement {
        return Init(thisVar)
    }

    override fun parse(): ClassFunction {
        return ClassFunction(ctx, enclosing)
    }

    private inner class Init(val thisVar: String): Statement {
        override fun toString(): String {
            return "$thisVar->$name = &$fullName"
        }
    }

}

class ClassFunction(ctx: Kotlin.FunctionDeclarationContext, enclosing: Type): Function() {
    override val header = ClassFunctionHeader(ctx, enclosing)
    override val block: CodeBlock

    val classVar = "this"

    init {
        val clazz = SymbolTable[enclosing] ?: throw RuntimeException("Cannot find type for $enclosing.")

        SymbolTable.push()

        // Create Variables
        clazz.list.forEach { SymbolTable += VarModifier(it) }
        parameters.forEach {
            SymbolTable.addVariable(it.name, it.type)
        }

        // Create this
        SymbolTable.thisType = enclosing

        block = Parser.parse(ctx.functionBody().block())
        SymbolTable.pop()
    }

    inner class VarModifier(variable: Variable): VariableMod(variable) {
        override fun get(): String {
            return "$classVar->${variable.get()}"
        }

        override fun set(): String {
            return "$classVar->${variable.set()}"
        }
    }

    override fun toString(): String {
        return "$header { $block } "
    }
}
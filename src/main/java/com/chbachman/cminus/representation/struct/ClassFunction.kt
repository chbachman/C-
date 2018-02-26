package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.SymbolTable
import com.chbachman.cminus.Variable
import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.representation.*
import com.chbachman.cminus.representation.function.ContextFuncHeader
import com.chbachman.cminus.representation.function.Func
import com.chbachman.cminus.representation.function.FuncHeader
import com.chbachman.cminus.representation.function.Parameter
import com.chbachman.cminus.static.Constants

class ClassFunctionHeader(ctx: Kotlin.FunctionDeclarationContext, enclosing: Type): FuncHeader() {
    override val name: String
    override val fullName: String
    override val parameters: List<Parameter>
    override val returnType: Type

    init {
        // Just to conveniently get most of the details.
        val tempHeader = ContextFuncHeader(ctx)
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

    private inner class Init(val thisVar: String): Statement {
        override fun toString(): String {
            return "$thisVar->$name = &$fullName"
        }
    }

}

class ClassFunction(ctx: Kotlin.FunctionDeclarationContext, enclosing: Type): Func() {
    override val header = ClassFunctionHeader(ctx, enclosing)
    val block: CodeBlock

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
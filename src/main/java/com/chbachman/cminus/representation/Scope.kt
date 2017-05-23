package com.chbachman.cminus.representation

import com.chbachman.cminus.representation.function.CodeBlock
import com.chbachman.cminus.representation.function.FunctionHeader
import com.chbachman.cminus.representation.function.Header
import com.chbachman.cminus.representation.struct.StructHeader
import com.chbachman.cminus.representation.value.Variable
import com.chbachman.cminus.util.*

/**
 * Created by Chandler on 4/12/17.
 * Handles the declaration of variables. Uses a stack to represent the current scope.
 * Also handles structs and function declarations, but on a global scope.
 */
class Scope(functions: List<Header>, structs: List<StructHeader>) {

    private val stack = mutableListOf<ScopeHolder>()
    val structs = structs.associateBy { it.name }
    val functions = functions.groupBy { it.baseName }

    // Set the "this" containing variable.
    fun setThis(variable: Variable, container: VariableHolder) {
        stack.peek()?.thisVar = Pair(variable, container)
    }

    // Remove the current scope.
    fun popScope(): CodeBlock {
        return stack.pop()?.block ?: throw RuntimeException("Could not pop empty stack.")
    }

    // Add to scope.
    fun pushScope(block: CodeBlock) {
        stack.push(ScopeHolder(block))
        block.setupScope(this)
    }

    fun addVariable(v: Variable) {
        stack.peek()?.vars?.put(v.name, v)
    }

    fun getVariable(name: String): Variable? {

        for (scope in stack) {
            if (scope.vars.containsKey(name)) {
                return scope.vars[name]
            }

            val thisVar = scope.getThis(name)

            if (thisVar != null) {
                return thisVar
            }
        }

        return null
    }

    private inner class ScopeHolder(val block: CodeBlock) {
        var vars: MutableMap<String, Variable> = hashMapOf()
        var thisVar: Pair<Variable, VariableHolder>? = null

        fun getThis(name: String): Variable? {
            return Variable(thisVar?.first ?: return null, thisVar?.second?.getVariable(name) ?: return null)
        }
    }
}

operator fun Map<String, List<Header>>.get(name: String, parameters: List<Typed>): Header? {
    val functions = this[name]

    functions ?: return null

    return functions.find { it.matches(name, parameters) }
}


package com.chbachman.cminus.representation

import com.chbachman.cminus.representation.function.CodeBlock
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.value.Variable

import java.util.*

/**
 * Created by Chandler on 4/12/17.
 * Handles the declaration of variables. Uses a stack to represent the current scope.
 * Also handles structs and function declarations, but on a global scope.
 */
class Scope {

    private val stack = ArrayDeque<ScopeHolder>()
    private val structs = TreeMap<String, Struct>()

    // Set the "this" containing variable.
    fun setThis(variable: Variable, container: VariableHolder) {
        stack.peek().thisVar = Pair(variable, container)
    }

    // Remove the current scope.
    fun popScope(): CodeBlock {
        return stack.pop().block
    }

    // Add to scope.
    fun pushScope(block: CodeBlock) {
        stack.push(ScopeHolder(block))
        block.setupScope(this)
    }

    fun addVariable(v: Variable) {
        stack.peek().vars.put(v.name, v)
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

    fun addFunction(f: Function?) {
        if (f == null) {
            return
        }

        if (getFunction(f.baseName, f.parameters) != null) {
            throw RuntimeException("The function ${f.baseName} already exists.")
        }

        if (stack.peek().functions.containsKey(f.baseName)) {
            stack.peek().functions[f.baseName]!!.add(f)
            return
        }

        val newList = ArrayList<Function>()
        newList.add(f)

        stack.peek().functions.put(f.baseName, newList)
    }

    fun addStruct(s: Struct) {
        structs.put(s.name, s)
    }

    fun getStruct(name: String): Struct? {
        return structs[name]
    }

    fun getFunction(name: String, parameters: List<Typed>): Function? {
        return stack.find { scope ->
            scope.functions.containsKey(name)
        }?.functions?.get(name)?.find { f ->
            f.matches(name, parameters)
        }
    }

    fun functionExists(name: String): Boolean {
        return stack.find { scope ->
            scope.functions.containsKey(name)
        } !== null
    }

    fun functionExists(name: String, parameters: List<Typed>): Boolean {
        return getFunction(name, parameters) !== null
    }

    internal inner class ScopeHolder(val block: CodeBlock) {
        var vars: MutableMap<String, Variable> = TreeMap()
        var functions: MutableMap<String, MutableList<Function>> = TreeMap()

        var thisVar: Pair<Variable, VariableHolder>? = null

        fun getThis(name: String): Variable? {
            return Variable(thisVar?.first ?: return null, thisVar?.second?.getVariable(name) ?: return null)
        }
    }
}

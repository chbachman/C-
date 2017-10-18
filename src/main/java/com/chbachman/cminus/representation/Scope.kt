package com.chbachman.cminus.representation

import com.chbachman.cminus.representation.function.CodeBlock
import com.chbachman.cminus.representation.function.Header
import com.chbachman.cminus.representation.struct.StructHeader
import com.chbachman.cminus.representation.value.Identifier
import com.chbachman.cminus.representation.value.Variable
import com.chbachman.cminus.util.peek
import com.chbachman.cminus.util.pop
import com.chbachman.cminus.util.push

/**
 * Created by Chandler on 4/12/17.
 * Handles the declaration of variables. Uses a stack to represent the current scope.
 * Also handles structs and function declarations, but on a global scope.
 */
class Scope(functions: List<Header>, structs: List<StructHeader>) {

    private val stack = mutableListOf<ScopeHolder>()
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
        stack.peek()?.vars?.put(v.name.last.text, v)
    }

    fun getVariable(id: Identifier): Variable? {
        // The initial variable name
        val first = handleAllButLast(id) ?: return null

        return first + (id.last.getVar(first) ?: return null)
    }

    private fun handleAllButLast(id: Identifier): Variable? {
        val segments = id.segments.dropLast(1)
        val it = segments.iterator()

        // Get the first var. If we don't have it, then just return.
        val start = getFirstVariable(it) ?: return null

        // If it exhausts the full range, then we just return the self variable.
        if (!it.hasNext()) {
            return start
        }

        return getVariable(it, start)
    }

    private fun getFirstVariable(id: Iterator<Identifier.Segment>): Variable? {
        if (!id.hasNext()) {
            return null
        }

        val name = run {
            val name = id.next().text

            if (name == "self") {
                if (!id.hasNext()) {
                    return stack.peek()?.thisVar?.first
                }

                id.next().text
            } else {
                name
            }
        }

        val self = name == "self"

        for (scope in stack) {
            if (!self && scope.vars.containsKey(name)) {
                return scope.vars[name]
            }

            val thisVar = scope.getThis(name)

            if (thisVar != null) {
                return thisVar
            }
        }

        return null
    }

    private tailrec fun getVariable(id: Iterator<Identifier.Segment>, con: Variable): Variable? {
        // Since we don't have another one, we can just return this one.
        if (!id.hasNext()) {
            return con
        }

        val segment = id.next()

        // Have the segment get the variable from this one.
        val variable = segment.getVar(con) ?: return null

        return getVariable(id, variable)
    }

    private inner class ScopeHolder(val block: CodeBlock) {
        var vars: MutableMap<String, Variable> = hashMapOf()
        var thisVar: Pair<Variable, VariableHolder>? = null

        fun getThis(name: String): Variable? {
            return (thisVar?.first ?: return null) +
                    (thisVar?.second?.getVariable(name) ?: return null)
        }
    }
}

operator fun Map<String, List<Header>>.get(name: String, parameters: List<Typed>): Header? {
    val functions = this[name]

    functions ?: return null

    return functions.find { it.matches(name, parameters) }
}


package com.chbachman.cminus

import com.chbachman.cminus.representation.FuncHeader
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.util.peek
import com.chbachman.cminus.util.pop
import com.chbachman.cminus.util.push

object ScopeStack {

    private val stack = mutableListOf<Scope>()
    private val funcMap = mutableMapOf<FunctionKey, FuncHeader>()

    fun pop() {
        stack.pop()
    }

    fun push() {
        stack.push(Scope())
    }

    fun addVariable(name: String, type: Type) {
        stack.peek()?.variables?.put(name, type)
    }

    fun getVariable(name: String): Type? {
        return stack.peek()?.variables?.get(name)
    }

    fun addFunc(func: FuncHeader) {
        val types = func.parameters.map { it.type }
        val key = FunctionKey(func.name, types)

        funcMap.put(key, func)
    }

    fun getFunc(name: String, args: List<Type>): FuncHeader? {
        val key = FunctionKey(name, args)
        return funcMap[key]
    }

    fun hasFuncWithName(name: String): Boolean {
        return funcMap.any { it.key.name == name }
    }
}

// Used for storing function from name and list of parameters.
private data class FunctionKey(val name: String, val parameters: List<Type>)

class Scope {
    val variables = mutableMapOf<String, Type>()
}
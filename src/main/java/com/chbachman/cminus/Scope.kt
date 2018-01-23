package com.chbachman.cminus

import com.chbachman.cminus.representation.Func
import com.chbachman.cminus.representation.FuncHeader
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.util.peek
import com.chbachman.cminus.util.pop
import com.chbachman.cminus.util.push

typealias FuncHandler = (String, List<Type>) -> Func

object ScopeStack {
    private val stack = mutableListOf<Scope>()
    private val funcMap = mutableMapOf<FunctionKey, FuncHeader>()
    private val funcHandlers = mutableListOf<FuncHandler>()

    fun pop() {
        stack.pop()
    }

    fun push() {
        stack.push(Scope())
    }

    fun addVariable(name: String, type: Type) {
        stack.peek().variables[name] = type
    }

    fun getVariable(name: String): Type? {
        return stack.peek().variables[name]
    }

    fun addHandler(func: FuncHandler) {
        funcHandlers.add(func)
    }

    fun addFunc(func: FuncHeader) {
        val types = func.parameters.map { it.type }
        val key = FunctionKey(func.name, types)

        funcMap.put(key, func)
    }

    fun getFunc(name: String, args: List<Type>): List<FuncHeader> {
        return funcMap.filter { (key, _) ->
            if (key.name != name) {
                false
            } else {
                // Second -> First
                // Passing Args -> Function Args
                key.parameters.zip(args).all { it.second.canConvert(it.first) } &&
                key.parameters.size == args.size
            }
        }.map { it.value }
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
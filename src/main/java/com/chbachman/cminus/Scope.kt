package com.chbachman.cminus

import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.function.FuncHeader
import com.chbachman.cminus.util.peek
import com.chbachman.cminus.util.pop
import com.chbachman.cminus.util.push

typealias VariableTransform = (String, Type) -> String

object NameTable {
    private val stack = mutableListOf<Scope>()
    private val funcMap = mutableMapOf<FunctionKey, FuncHeader>()

    fun pop() {
        stack.pop()
    }

    fun push() {
        stack.push(Scope())
    }

    fun addVariable(name: String, type: Type, transform: VariableTransform? = null) {
        stack.peek() += ScopeVariable(name, type, transform)
    }

    fun getVariable(name: String): Pair<String, Type>? {
        return stack.mapNotNull { it.variables[name] }.firstOrNull()?.invoke()
    }

    operator fun plusAssign(func: FuncHeader) {
        val types = func.parameters.map { it.type }
        val key = FunctionKey(func.name, types)

        funcMap[key] = func
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
    val variables = mutableMapOf<String, ScopeVariable>()

    operator fun plusAssign(scope: ScopeVariable) {
        variables[scope.name] = scope
    }
}

data class ScopeVariable(
    val name: String,
    val type: Type,
    val transform: VariableTransform?
) {
    operator fun invoke(): Pair<String, Type> {
        val fullName = transform?.invoke(name, type) ?: name

        return Pair(
            fullName,
            type
        )
    }
}
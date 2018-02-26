package com.chbachman.cminus

import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.function.FuncHeader
import com.chbachman.cminus.util.peek
import com.chbachman.cminus.util.pop
import com.chbachman.cminus.util.push

typealias VariableTransform = (String, Type) -> String

interface Variable {
    val name: String
    val type: Type

    fun get(): String { return name }
    fun set(): String { return name }
}

object SymbolTable: Namespace() {
    private val stack = mutableListOf<Scope>()
    private val classes = mutableMapOf<Type, Namespace>()

    override val list: Collection<Variable>
        get() {
            return stack.flatMap { it.variables.values }
        }

    override val variables: Scope
        get() = stack.peek()

    var thisType: Type?
        get() {
            return variables.thisType
        }
        set(value) {
            variables.thisType = value
        }

    fun pop() {
        stack.pop()
    }

    fun push() {
        stack.push(Scope())
    }

    // Get Variables
    override operator fun get(name: String): Variable? {
        return stack.mapNotNull { it.variables[name] }.firstOrNull()
    }

    // Operations for Class Functions
    operator fun set(type: Type, func: Namespace) {
        classes[type] = func
    }

    operator fun get(type: Type): Namespace? {
        return classes[type]
    }
}

open class Namespace {
    private val functions = mutableMapOf<FunctionKey, FuncHeader>()
    protected open val variables = Scope()

    open val list: Collection<Variable>
        get() = variables.variables.values

    // Function Handling
    operator fun plusAssign(func: FuncHeader) {
        val types = func.parameters.map { it.type }
        val key = FunctionKey(func.name, types)

        functions[key] = func
    }

    operator fun plusAssign(functions: List<FuncHeader>) {
        functions.forEach { plusAssign(it) }
    }

    operator fun get(name: String, args: List<Type>): List<FuncHeader> {
        return functions.filter { (key, _) ->
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
        return functions.any { it.key.name == name }
    }

    // Variable Handling
    fun addVariable(name: String, type: Type, transform: VariableTransform? = null) {
        variables += ScopeVariable(name, type, transform)
    }

    open operator fun get(name: String): Variable? {
        return variables.variables[name]
    }

    operator fun plusAssign(variable: Variable) {
        variables += variable
    }
}

// Used for storing function from name and list of parameters.
private data class FunctionKey(val name: String, val parameters: List<Type>)

class Scope {
    val variables = mutableMapOf<String, Variable>()
    var thisType: Type? = null

    operator fun plusAssign(scope: Variable) {
        variables[scope.name] = scope
    }
}

private data class ScopeVariable(
    override val name: String,
    override val type: Type,
    val transform: VariableTransform?
): Variable {
    operator fun invoke(): Pair<String, Type> {
        val fullName = transform?.invoke(name, type) ?: name

        return Pair(
            fullName,
            type
        )
    }

    override fun toString(): String {
        return name
    }
}
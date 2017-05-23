package com.chbachman.cminus.representation.function

import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Typed
import com.chbachman.cminus.representation.value.Variable

/**
 * Created by Chandler on 5/22/17.
 */
abstract class Header: Typed {
    abstract val baseName: String
    abstract val parameters: List<Variable>
    open val overloaded = true

    abstract fun getFunc(scope: Scope): FunctionType

    open val cName: String
        get() {
            val b = StringBuilder()

            if (overloaded) {
                for (v in parameters) {
                    b.append(v.type.typeName.toLowerCase())
                    b.append('$')
                }
            }

            b.append(baseName)
            return b.toString()
        }

    open val header: String
        get() {
            val b = StringBuilder("${type.code()} $cName (")

            for (p in parameters) {
                b.append("${p.type.code()} ${p.name}, ")
            }

            // Get rid of the last ", "
            if (!parameters.isEmpty()) {
                b.delete(b.length - 2, b.length)
            }

            b.append(");")

            return b.toString()
        }

    // Checks to make sure the parameters match this function.
    fun matches(name: String, parameters: List<Typed>): Boolean {
        if (name != this.baseName) {
            return false
        }

        if (parameters.size != this.parameters.size) {
            return false
        }

        for (i in parameters.indices) {
            if (parameters[i].type != this.parameters[i].type) {
                return false
            }
        }

        return true
    }
}
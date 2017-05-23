package com.chbachman.cminus.representation.struct

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.*
import com.chbachman.cminus.representation.function.CodeBlock
import com.chbachman.cminus.representation.function.FunctionHeader
import com.chbachman.cminus.representation.function.Header
import com.chbachman.cminus.representation.value.Variable

/**
 * Created by Chandler on 4/15/17.
 * Handles structs in C-
 * Converts them to C structs and creates methods to allow for initialization.
 */
class Struct(val header: StructHeader, scope: Scope) : CodeBlock, Typed, VariableHolder {

    // TODO: init / deinit blocks? How will deinit work?
    val name: String = header.name
    override val type = header.type
    val variables = header.ctx.classBlock().variable().map { Variable(it, scope) }
    val inits = header.inits.map { InitBlock(it, this, scope) }

    override fun getVariable(name: String): Variable? {
        return variables.find { it.name == name }
    }

    override val first = "struct $name {"

    override val middle: String
        get() {
            val vars = StringBuilder()

            for (v in variables) {
                // Convert variable to one without a value.
                vars.append(Variable(v.name, v.type).code())
            }

            return vars.toString()
        }

    override val last = "};\n"
}

class StructHeader(val ctx: CMinusParser.StructContext): Typed {
    val name: String = ctx.ID().text
    val functions = ctx.classBlock().func().map { FunctionHeader(it) }
    val inits = ctx.classBlock().initBlock().map { InitHeader(it, this) }
    override val type: Type
        get() = Type.from(this)

    fun getStruct(scope: Scope): Struct {
        return Struct(this, scope)
    }
}

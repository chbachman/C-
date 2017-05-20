package com.chbachman.cminus.representation

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.function.CodeBlock
import com.chbachman.cminus.representation.value.Variable
import java.util.Optional
import java.util.stream.Collectors

/**
 * Created by Chandler on 4/15/17.
 * Handles structs in C-
 * Converts them to C structs and creates methods to allow for initialization.
 */
class Struct(ctx: CMinusParser.StructContext, scope: Scope) : CodeBlock, Typed, VariableHolder {

    // TODO: init / deinit blocks? How will deinit work?
    val name: String = ctx.ID().text
    private val variables = ctx.classBlock().variable().map { Variable(it, scope) }
    private val inits = ctx.classBlock().initBlock().map { InitBlock(it, this, scope) }

    override fun getVariable(name: String): Variable? {
        return variables.find { it.name == name }
    }

    fun getInit(name: String, parameters: List<Typed>): InitBlock? {
        return inits.find { it.matches("init" + name, parameters) }
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
    override val last: String
        get() {
            val initFunc = StringBuilder("};\n")

            for (f in inits) {
                initFunc.append(f.code())
            }

            return initFunc.toString()
        }

    override val type = Type.from(this)
}

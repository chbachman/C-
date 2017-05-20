package com.chbachman.cminus.representation.function

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.Typed
import com.chbachman.cminus.representation.statement.Statement
import com.chbachman.cminus.representation.value.Variable

/**
 * Created by Chandler on 4/9/17.
 * Representation of Function.
 */
open class Function : CodeBlockHolder, Typed {

    override val type: Type
    override val statements: List<Statement>
    val baseName: String
    val parameters: List<Variable>

    constructor(ctx: CMinusParser.FuncContext, scope: Scope) {
        scope.pushScope(this)
        statements = Parser.parse(ctx.codeBlock(), scope)
        scope.popScope()
        type = if (ctx.funcReturn() != null) Type.from(ctx.funcReturn().type()) else Type.Native.VOID.type
        baseName = ctx.ID().text

        if (ctx.parameterList() != null) {
            parameters = ctx.parameterList().parameter().map { Variable(it) }
        } else {
            parameters = listOf()
        }
    }

    protected constructor(type: Type, name: String, statements: List<Statement>) {
        this.type = type
        this.baseName = name
        this.parameters = listOf()
        this.statements = statements
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

    val cName: String
        get() {
            val b = StringBuilder()
            for (v in parameters) {
                b.append(v.type.typeName.toLowerCase())
                b.append('$')
            }
            b.append(baseName)
            return b.toString()
        }

    val header: String
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

    override val first: String
        get() {
            val b = StringBuilder().append(header)
            // Get rid of semicolon, add {
            b.deleteCharAt(b.length - 1).append(" {")

            return b.toString()
        }

    override val last = "}\n"
}
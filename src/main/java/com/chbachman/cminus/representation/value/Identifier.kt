package com.chbachman.cminus.representation.value

import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.get
import com.chbachman.cminus.representation.statement.Call
import com.chbachman.cminus.representation.statement.StructConstructor

/**
 * Created by Chandler on 5/22/17.
 * Handles the creation and parsing of Identifiers
 */
class Identifier private constructor(val segments: List<Segment>) {

    val first: Segment
        get() = segments.first()

    val last: Segment
        get() = segments.last()

    val text: String
        get() = segments.map { it.text }.joinToString(".")

    constructor(ctx: CMinusParser.IdentifierContext, scope: Scope):
            this(ctx.segment().map {
                parseSegment(it, scope)
            })

    constructor(ctx: CMinusParser.FunctionCallContext, scope: Scope):
            this(ctx.segment().map {
                parseSegment(it, scope)
            } + FuncSegment(ctx.funcSegment(), scope))

    constructor(vararg name: String):
            this(name.map { IDSegment(it) })

    operator fun plus(other: Identifier): Identifier {
        return Identifier(segments + other.segments)
    }

    operator override fun equals(other: Any?): Boolean {
        if (other is Identifier) {
            return other.segments == this.segments
        }

        return false
    }

    override fun hashCode(): Int {
        return segments.hashCode()
    }

    override fun toString(): String {
        return text
    }

    abstract class Segment {
        abstract val text: String

        abstract fun getType(parent: Type) : Type?

        override fun toString(): String {
            return text
        }
    }

    class IDSegment(override val text: String, scope: Scope): Segment() {
        constructor(ctx: CMinusParser.IdSegmentContext, scope: Scope): this(ctx.ID().text, scope)


        override fun getType(parent: Type): Type? {
            val s = Type[parent]?.
            val variable = s.struct.getVariable(text) ?: return null

            return
        }
    }

    class FuncSegment(val ctx: CMinusParser.FuncSegmentContext, scope: Scope): Segment(), Call {
        override val parameters = Parser.parse(ctx.argumentList(), scope)
        override val ref = scope.functions[name.text, parameters] ?: throw RuntimeException("Function $name was not found.")
        override val text: String
            get() = expression

        override val name: Identifier
            get() = Identifier(text)

        override fun getType(parent: Type): Type? {
            return Type[parent]?.functions?.find { it.matches(text, parameters) }?.type
        }
    }

    companion object {
        private fun parseSegment(ctx: CMinusParser.SegmentContext, scope: Scope): Segment {
            if (ctx.idSegment() != null) {
                return IDSegment(ctx.idSegment(), scope)
            }

            if (ctx.funcSegment() != null) {
                return FuncSegment(ctx.funcSegment(), scope)
            }

            throw RuntimeException("ID Segment not implemented. ${ctx.text}")
        }
    }
}
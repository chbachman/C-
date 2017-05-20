package com.chbachman.cminus

import com.chbachman.cminus.gen.CMinusBaseListener
import com.chbachman.cminus.gen.CMinusLexer
import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Struct
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.function.MainFunction
import com.chbachman.cminus.representation.value.Variable
import com.chbachman.cminus.util.Run
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File
import java.io.PrintStream
import java.util.*

class Start constructor(inputPath: String, outputPath: String) {

    val input = File(inputPath)
    val output = File(outputPath)

    private var parser = generateTree(CharStreams.fromFileName(inputPath))
    private var out = PrintStream(output)
    var run = false

    init {
        val tree = parser.init()
        val scope = Scope()
        val walker = ParseTreeWalker()

        // TODO: Make this more general
        // Pass 0: Add external runtime libraries.
        out.println("#include <stdio.h>")
        out.println()

        // Something the walker does creates the tree.
        // So we do a dry run to init everything before accessing it ourselves.
        walker.walk(NOOP(), tree)

        init(tree.statements(), scope)

        // Why bother trying to format it ourselves, when we can just have clang do it for us.
        Run.command("clang-format -i " + output.canonicalPath)

        if (run) {
            Run.buildAndRun(output.canonicalPath, "./build/temp")
        }
    }

    constructor(inputPath: String, outputPath: String, run: Boolean): this(inputPath, outputPath) {
        this.run = run
    }

    internal fun generateTree(input: CharStream): CMinusParser {
        val lexer = CMinusLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = CMinusParser(tokens)
        return parser
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            if (args.size < 2) {
                throw RuntimeException("Need input and output files.")
            }

            Type.init()
            Start(args[0], args[1], true)
        }
    }

    fun init(ctx: CMinusParser.StatementsContext, scope: Scope) {

        for (s in ctx.struct()) {
            val struct = Struct(s, scope)

            scope.addStruct(struct)

            out.println(struct.first)
            out.println(struct.middle)
            out.println(struct.last)
        }

        createFunctions(ctx.func(), scope)

        // Handle Main Function
        val f = MainFunction()
        out.println(f.first)
        scope.pushScope(f)

        for (s in ctx.statement()) {
            val statement = Parser.parse(s, scope)
            if (statement is Variable) {
                scope.addVariable(statement)
            }
            out.println(statement.code())
        }

        out.println(scope.popScope().last)
    }

    fun createFunctions(input: List<CMinusParser.FuncContext>, scope: Scope) {
        val functions = ArrayList<Function>(input.size)

        for (f in input) {
            val func = Function(f, scope)
            functions.add(func)
        }

        for (f in functions) {
            out.println(f.header)
        }

        out.println()

        for (f in functions) {
            out.println(f.code())
            out.println()
        }

        out.println()
    }

    class NOOP : CMinusBaseListener()
}



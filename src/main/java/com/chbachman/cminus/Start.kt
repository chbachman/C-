package com.chbachman.cminus

import com.chbachman.cminus.gen.CMinusBaseListener
import com.chbachman.cminus.gen.CMinusLexer
import com.chbachman.cminus.gen.CMinusParser
import com.chbachman.cminus.representation.Parser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.function.FunctionHeader
import com.chbachman.cminus.representation.function.MainFunction
import com.chbachman.cminus.representation.struct.StructHeader
import com.chbachman.cminus.representation.value.Variable
import com.chbachman.cminus.util.Run
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File
import java.io.PrintStream

class Start constructor(inputPath: String, outputPath: String, run: Boolean = true) {

    // Main Method, all it does is init Type and create Start.
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            if (args.size < 2) {
                throw RuntimeException("Need input and output files.")
            }

            Start(args[0], args[1], true)
        }
    }

    val input = File(inputPath)
    val output = File(outputPath)

    private val parser = generateTree(CharStreams.fromFileName(inputPath))
    private val out = PrintStream(output)

    init {
        val tree = parser.init()
        val walker = ParseTreeWalker()

        // TODO: Make this more general
        // Pass 0: Add external runtime libraries.
        out.println("#include <stdio.h>")
        out.println()

        // Something the walker does creates the tree.
        // So we do a dry run to init everything before accessing it ourselves.
        walker.walk(NOOP(), tree)

        init(tree.statements())

        // Why bother trying to format it ourselves, when we can just have clang do it for us.
        Run.command("clang-format -i " + output.canonicalPath)

        if (run) {
            Run.buildAndRun(output.canonicalPath, "./build/temp")
        }
    }

    internal fun generateTree(input: CharStream): CMinusParser {
        val lexer = CMinusLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = CMinusParser(tokens)
        return parser
    }

    fun init(ctx: CMinusParser.StatementsContext) {
        val structs = ctx.struct().map { StructHeader(it) }

        // Every "Function Header" has to implement a conversion to a regular FunctionType
        // This allows pre-declaration of all the global functions.
        // Somewhere along the line, functions need to either get qualified with scope or name
        val functions = ctx.func().map { FunctionHeader(it) } + structs.flatMap { it.inits }

        val scope = Scope(functions, structs)

        scope.structs.values.forEach {
            out.println(it.getStruct(scope).statement)
        }

        // Get all function headers, and print them all out.
        scope.functions.flatMap {
            it.value
        }.forEach {
            out.println(it.header)
        }

        out.println()

        // Get all regular functions, and print those out too.
        scope.functions.flatMap {
            it.value.map { it.getFunc(scope) }
        }.forEach {
            out.println(it.statement)
        }

        // Handle Main Function
        val main = MainFunction()
        out.println(main.first)
        scope.pushScope(main)

        ctx.statement().map {
            Parser.parse(it, scope)
        }.forEach {
            if (it is Variable) {
                scope.addVariable(it)
            }
            out.println(it.statement)
        }

        out.println(scope.popScope().last)
    }

    class NOOP : CMinusBaseListener()
}



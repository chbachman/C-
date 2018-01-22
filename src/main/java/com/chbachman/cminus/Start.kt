package com.chbachman.cminus

import com.chbachman.cminus.gen.Kotlin
import com.chbachman.cminus.gen.KotlinBaseListener
import com.chbachman.cminus.gen.KotlinLexer
import com.chbachman.cminus.representation.FuncHeader
import com.chbachman.cminus.representation.Parser
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
        val tree = parser.kotlinFile()
        val walker = ParseTreeWalker()

        // TODO: Make this more general
        // Pass 0: Add external runtime libraries.
        out.println("#include <stdio.h>")
        out.println()

        // Something the walker does creates the tree.
        // So we do a dry run to init everything before accessing it ourselves.
        walker.walk(NOOP(), tree)

        init(tree)

        // Why bother trying to format it ourselves, when we can just have clang do it for us.
        Run.command("clang-format -i " + output.canonicalPath)

        if (run) {
            Run.buildAndRun(output.canonicalPath, "./build/temp")
        }
    }

    private fun generateTree(input: CharStream): Kotlin {
        val lexer = KotlinLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = Kotlin(tokens)
        return parser
    }

    fun init(ctx: Kotlin.KotlinFileContext) {

        ctx.topLevelObject().mapNotNull {
            it.functionDeclaration()
        }.forEach {
            ScopeStack.addFunc(FuncHeader(it))
        }

        // Handle Main Function
        ctx.topLevelObject().map {
            Parser.parse(it)
        }.forEach {
            out.println(it)
        }
    }

    class NOOP : KotlinBaseListener()
}



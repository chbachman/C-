package com.chbachman.cminus.util

import com.chbachman.cminus.Start
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by Chandler on 4/15/17.
 * Runs commands on the command line.
 * Also handles building and running C and C Minus files, for convenience.
 */
object Run {

    fun buildCM(inputFile: File, outputFile: File) {
        Start(inputFile.canonicalPath, outputFile.canonicalPath, false)
    }

    fun build(inputFile: File, outputFile: File): Output {
        return command("clang -Wno-main-return-type ${inputFile.path} -o ${outputFile.path}")
    }

    fun run(inputFile: File): Output {
        return command(inputFile.canonicalPath)
    }

    fun buildAndRun(inputFile: File, tempFile: File) {
        build(inputFile, tempFile)
        run(tempFile)
    }

    fun series(commands: Array<String>) {
        for (command in commands) {
            _command(command).join()
        }
    }

    fun command(command: String): Output {
        val output = StringBuilder()
        val err = StringBuilder()

        _command(command, output, err).join()

        return Output(output.toString(), err.toString())
    }

    private fun _command(
        command: String,
        output: Appendable = System.out,
        error: Appendable = System.err
    ): Thread {
        try {
            val pr = Runtime.getRuntime().exec(command)

            val thread = Thread {
                pr.waitFor()

                val input = BufferedReader(InputStreamReader(pr.inputStream))
                val err = BufferedReader(InputStreamReader(pr.errorStream))
                var line = input.readLine()

                while (line != null) {
                    output.append(line)
                    output.append('\n')
                    line = input.readLine()
                }

                line = err.readLine()

                while (line != null) {
                    error.append(line)
                    error.append('\n')
                    line = err.readLine()
                }
            }

            thread.start()

            return thread

        } catch (exception: IOException) {
            throw IOException("Some error occured: ", exception)
        }
    }
}

data class Output(
    val out: String,
    val err: String
) {
    val empty: Boolean
    get() {
        return out.isBlank() && err.isBlank()
    }

    fun print(): Output {
        if (empty) {
            return this
        }

        println("Out: ")
        println(out)

        println("Err: ")
        println(err)

        return this
    }
}
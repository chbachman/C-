package com.chbachman.cminus.util

import com.chbachman.cminus.Start
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by Chandler on 4/15/17.
 * Runs commands on the command line.
 * Also handles building and running C and C Minus files, for convenience.
 */
object Run {

    fun buildCM(inputFile: String, outputFile: String) {
        Start(inputFile, outputFile, false)
    }

    fun build(inputFile: String, outputFile: String): String {
        return command("clang $inputFile -o $outputFile")
    }

    fun run(inputFile: String): String {
        return command(inputFile)
    }

    fun buildAndRun(inputFile: String, tempFile: String) {
        build(inputFile, tempFile)
        run(tempFile)
    }

    fun series(commands: Array<String>) {
        for (command in commands) {
            _command(command).waitFor()
        }
    }

    fun command(command: String): String {
        val builder = StringBuilder()

        _command(command, builder).waitFor()

        return builder.toString()
    }

    private fun _command(command: String, output: Appendable = System.out, error: Appendable = System.err): Process {
        val pr = Runtime.getRuntime().exec(command)

        Thread {
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
        }.start()

        return pr
    }
}

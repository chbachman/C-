package com.chbachman.cminus.util

import org.testng.Assert.assertEquals
import java.io.File

internal object TestProgram {
    private val testFileDir = File("./src/test/cm")

    private fun test(program: File) {
        val tempDir = createTempDir()
        tempDir.deleteOnExit()

        val cFile = File(tempDir, "temp.c")
        val executable = File(tempDir, "temp")
        val kotlinScript = File(tempDir, "temp.kts")

        Run.buildCM(program, cFile)
        Run.build(cFile, executable)

        val c = Run.run(executable)

        program.copyTo(kotlinScript, true)
        kotlinScript.appendText("\nmain()")

        val kotlin = Run.command("kotlinc -script ${kotlinScript.canonicalPath}")

        println("C Minus Output:")
        println(c.out)

        println("Kotlin Output:")
        println(kotlin.out)

        assertEquals(c, kotlin)
    }

    fun test(path: String) {
        test(File(testFileDir, path))
    }

}

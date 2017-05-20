package com.chbachman.cminus.util

import org.junit.Assert.assertEquals
import java.io.File

internal object TestProgram {

    private val testDir = File("./build/test/")
    private val tempFile = File(testDir, "temp.c")
    private val tempFile2 = File(testDir, "temp2")

    private val testFileDir = File("./src/test/cm")

    private fun test(program: File) {
        val cminus = program.canonicalPath
        val cFile = tempFile.canonicalPath
        val compiledFile = tempFile2.canonicalPath

        Run.buildCM(cminus, cFile)
        Run.build(cFile, compiledFile)

        val c = Run.run(compiledFile)
        val swift = Run.command("swift " + cminus)

        assertEquals(c, swift)
    }

    fun test(path: String) {
        test(File(testFileDir, path))
    }

}

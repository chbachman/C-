import com.chbachman.cminus.util.Output
import com.chbachman.cminus.util.Run
import org.testng.Assert.assertEquals
import org.testng.Assert.fail
import java.io.File
import java.io.IOException

internal object TestProgram {
    private val testFileDir = File("./src/test/cm")

    private fun test(program: File, outputTest: String? = null) {
        val tempDir = createTempDir()
        tempDir.deleteOnExit()

        val fileName = program.nameWithoutExtension

        val cFile = File(tempDir, "$fileName.c")
        val executable = File(tempDir, fileName)
        val kotlinScript = File(tempDir, "$fileName.kts")

        try {
            Run.buildCM(program, cFile)
            Run.build(cFile, executable).print()

            val c = Run.run(executable)

            // If we were not given any output to check against, check it against kotlin itself.
            val programOutput =
                if (outputTest == null) {
                    program.copyTo(kotlinScript, true)
                    kotlinScript.appendText("\nmain()")

                    Run.command("kotlinc -nowarn -script ${kotlinScript.canonicalPath}")
                } else {
                    Output(outputTest, "")
                }

            if (c != programOutput) {
                println("C Minus Output:")
                println(c.out)

                println("Kotlin Output:")
                println(programOutput.out)
            }

            assertEquals(c, programOutput)
        } catch (err: IOException) {
            val files = tempDir.listFiles().toList()
            print(files)

            files.forEach {
                it.readLines().forEach {
                    println(it)
                }
            }

            throw err
        }
    }

    fun testSimple(program: String, output: String) {
        val temp = createTempFile()
        temp.writeText(program)

        try {
            test(temp, output)
        } catch (err: IOException) {
            fail("Could not recover from:", err)
        }
    }

    fun test(path: String) {
        try {
            test(File(testFileDir, path))
        } catch (err: IOException) {
            fail("Could not recover from:", err)
        }
    }
}

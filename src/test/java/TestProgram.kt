import com.chbachman.cminus.util.Run
import org.testng.Assert.assertEquals
import org.testng.Assert.fail
import java.io.File
import java.io.IOException

internal object TestProgram {
    private val testFileDir = File("./src/test/cm")

    private fun test(program: File) {
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

            program.copyTo(kotlinScript, true)
            kotlinScript.appendText("\nmain()")

            val kotlin = Run.command("kotlinc -nowarn -script ${kotlinScript.canonicalPath}")

            if (c != kotlin) {
                println("C Minus Output:")
                println(c.out)

                println("Kotlin Output:")
                println(kotlin.out)
            }

            assertEquals(c, kotlin)
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

    fun test(path: String) {
        try {
            test(File(testFileDir, path))
        } catch (err: IOException) {
            fail("Could not recover from:", err)
        }
    }
}

import java.io.File

fun main(args: Array<String>) {
    val testDir = File("src/test/cm")
    val genDir = File("src/test/java/gen")

    val files = testDir
        .listFiles { file -> file.isDirectory }
        .map {
            val files = it.listFiles { file -> file.isFile }
            TestFile(files.map { it.name }, it.name)
        }

    genDir.delete()
    genDir.mkdir()

    files.forEach { testFile ->
        val className = testFile.parent.capitalize() + "Test"
        val toCreate = File(genDir, className + ".kt")

        toCreate.createNewFile()

        val writer = toCreate.printWriter().use { out ->
            out.println("""
                import org.testng.annotations.Test

                class $className {
            """.trimIndent())

            testFile.files.forEach {
                val name = it.removeSuffix(".cm").decapitalize()

                out.println("   @Test")
                out.println("   fun $name() {")
                out.println("       TestProgram.test(\"${testFile.parent}/$it\")")
                out.println("   }")
            }

            out.println("}")
        }
    }
}

data class TestFile (
    val files: List<String>,
    val parent: String
)
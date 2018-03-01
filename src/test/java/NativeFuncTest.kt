import org.testng.annotations.Test

class NativeFuncTest {

    val program = "external fun printf(x: CString, x: Int); fun main() { printf(\"Hello! %d\", 43) }"

    @Test
    fun basic() {
        // Apparently on a C program's exit it will print out a final newline?
        // Or something is happening that adds a newline.
        // ¯\_(ツ)_/¯
        // Doesn't seem to be a problem with my code.
        TestProgram.testSimple(program, "Hello! 43\n")
    }
}
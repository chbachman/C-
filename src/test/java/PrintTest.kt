package test.java

import com.chbachman.cminus.util.TestProgram
import org.junit.Test

class PrintTest {

    @Test
    fun printHello() {
        TestProgram.test("Print.cm")
    }

    @Test
    fun variableDeclaration() {
        TestProgram.test("Variable.cm")
    }

    @Test
    fun forLoopPrint() {
        TestProgram.test("ForLoop.cm")
    }

    @Test
    fun structPrint() {
        TestProgram.test("StructPrint.cm")
    }

    @Test
    fun parenPrint() {
        TestProgram.test("Parenthesis.cm")
    }

}

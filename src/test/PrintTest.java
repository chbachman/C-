package test;

import org.junit.Assert;
import org.junit.Test;

public class PrintTest {

    @Test
    public void printHello() {
        Assert.assertTrue(TestProgram.test("Print.cm"));
    }

    @Test
    public void variableDeclaration() {
        Assert.assertTrue(TestProgram.test("Variable.cm"));
    }

    @Test
    public void structPrint() {
        Assert.assertTrue(TestProgram.test("StructPrint.cm"));
    }
}

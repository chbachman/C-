import org.testng.annotations.Test

class FunctionTest {
   @Test
   fun functionCall() {
       TestProgram.test("function/FunctionCall.cm")
   }
   @Test
   fun overloadedFunction() {
       TestProgram.test("function/OverloadedFunction.cm")
   }
}

import org.testng.annotations.Test

class LiteralTest {
   @Test
   fun long() {
       TestProgram.test("literal/Long.cm")
   }
   @Test
   fun simple() {
       TestProgram.test("literal/Simple.cm")
   }
   @Test
   fun float() {
       TestProgram.test("literal/Float.cm")
   }
}

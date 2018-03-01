import org.testng.annotations.Test

class MathTest {
   @Test
   fun simple() {
       TestProgram.test("math/Simple.cm")
   }
   @Test
   fun order() {
       TestProgram.test("math/Order.cm")
   }
}

import org.testng.annotations.Test

class ClassTest {
   @Test
   fun basic() {
       TestProgram.test("class/Basic.cm")
   }
   @Test
   fun thisClass() {
       TestProgram.test("class/ThisClass.cm")
   }
   @Test
   fun methods() {
       TestProgram.test("class/Methods.cm")
   }
   @Test
   fun constructor() {
       TestProgram.test("class/Constructor.cm")
   }
}

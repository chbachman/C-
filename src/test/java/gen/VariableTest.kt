import org.testng.annotations.Test

class VariableTest {
   @Test
   fun assignment() {
       TestProgram.test("variable/Assignment.cm")
   }
   @Test
   fun simpleVar() {
       TestProgram.test("variable/SimpleVar.cm")
   }
}

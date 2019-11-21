import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class IntNextTest {

  @Test
  fun normal() {
    assertSame(3 , IntNext.next(2))
  }

  @Test
  fun testWith() {

    with(IntNext) {
      assertSame(2 , 1.next)
    }
  }
}

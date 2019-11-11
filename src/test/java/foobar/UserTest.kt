package foobar

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Created by kevin.huang on 2019-09-04.
 */
internal class UserTest {
  private val logger = KotlinLogging.logger { }

  @Test
  fun getUsername() {
    val user = User("username", "password")
    assertNotNull(user)
    assertEquals("username" , user.username)

    logger.info("username = {}", user.username)
  }
}
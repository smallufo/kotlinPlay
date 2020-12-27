/**
 * Created by smallufo on 2020-12-28.
 */
package foo

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RandomPhotoUrlPicsumTest {

  private val logger = KotlinLogging.logger {  }

  @Test
  fun testGet(): Unit = runBlocking {
    val impl = RandomPhotoUrlPicsum()
    impl.getPhotoUrl(1024 , 768).also {
      logger.info("url = {}" , it)
    }
  }
}

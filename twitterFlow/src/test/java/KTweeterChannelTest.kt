/**
 * Created by kevin.huang on 2019-09-06.
 */

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class KTweeterChannelTest {

  private val logger = KotlinLogging.logger {}

  @Test
  fun testChannel() {
    val channel = KTweeterChannel().channel

    runBlocking {
      while (true) {

        val t1 = measureTimeMillis {
          val status = channel.receive()
          logger.info("[{}] {} : {}", status.id, status.user.name, status.text)
        }
        logger.debug("receive message , takes {} millis", t1)

      }
    }
  }

}
/**
 * Created by kevin.huang on 2020-06-02.
 */
package foo

import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import kotlin.test.Test


@RunWith(SpringRunner::class)
@TestExecutionListeners(NowListenerWithoutContext::class)
class NowListenerWithoutContextTest {
  @Now
  private lateinit var nowTaipei: LocalDateTime

  @Now("Asia/Tokyo")
  private lateinit var nowTokyo: LocalDateTime

  @Now("GMT")
  private lateinit var nowGmt: LocalDateTime

  @Now("XXX")
  private lateinit var nowXXX: LocalDateTime

  private val logger = KotlinLogging.logger { }

  @Test
  fun printNow() {
    logger.info("Asia/Taipei = {}", nowTaipei)
    logger.info("Asia/Tokyo = {}", nowTokyo)
    logger.info("GMT = {}", nowGmt)
    logger.info("XXX (default) = {}", nowXXX)
  }
}
/**
 * Created by kevin.huang on 2020-06-02.
 */
package foo

import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.test.Test

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [SpringConfig::class])
@TestExecutionListeners(NowListenerWithContext::class)
class NowListenerWithContextTest {


  @Now
  private lateinit var nowTaipei: LocalDateTime

  @Now("Asia/Tokyo" , 10)
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
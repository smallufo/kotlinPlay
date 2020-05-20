/**
 * Created by smallufo on 2020-05-20.
 */
package foobar.caching.springBuiltIn

import foobar.caching.ICalculator
import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject
import kotlin.test.Test

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [SpringCacheConfig::class])
class CalculatorSpringCacheImplTest {

  @Inject
  private lateinit var service: ICalculator

  private val logger = KotlinLogging.logger { }

  @Test
  fun testCaching() {
    repeat(5) {
      service.areaOfCircle(10.0).also {
        logger.info("result = {}", it)
      }
    }
    repeat(5) {
      service.areaOfCircle(20.0).also {
        logger.info("result = {}", it)
      }
    }
  }
}

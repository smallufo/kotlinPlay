/**
 * Created by smallufo on 2020-05-20.
 */
package foobar.caching.ehcache

import foobar.caching.ICalculator
import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject
import kotlin.test.Test
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [EhcacheConfig::class])
@OptIn(ExperimentalTime::class)
class CalculatorEhcacheImplTest {

  @Inject
  private lateinit var service: ICalculator

  private val logger = KotlinLogging.logger { }

  @Test
  fun testCaching() {
    repeat(5) {
      measureTimedValue { service.areaOfCircle(10.0) }.also { r ->
        logger.info("result = {} , takes {}", r.value, r.duration)
      }
    }
    repeat(5) {
      measureTimedValue { service.areaOfCircle(20.0) }.also { r ->
        logger.info("result = {} , takes {}", r.value, r.duration)
      }
    }
  }
}

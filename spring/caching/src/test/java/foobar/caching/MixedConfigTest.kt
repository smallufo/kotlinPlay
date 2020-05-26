/**
 * Created by kevin.huang on 2020-05-26.
 */
package foobar.caching

import mu.KotlinLogging
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject
import javax.inject.Provider
import kotlin.test.Test
import kotlin.time.ExperimentalTime

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [MixedConfig::class])
@OptIn(ExperimentalTime::class)
class MixedConfigTest {

  @Inject
  private lateinit var calculators: Provider<List<ICalculator>>

  private val logger = KotlinLogging.logger { }

  @Test
  fun printAllCalculators() {
    assertNotNull(calculators)

    calculators.get().forEachIndexed { index, cal ->
      logger.info("[{}] proxied class = {}", index, cal.javaClass)
    }
  }
}
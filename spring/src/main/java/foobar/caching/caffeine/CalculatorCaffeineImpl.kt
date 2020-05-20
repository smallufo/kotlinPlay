/**
 * Created by kevin.huang on 2020-05-20.
 */
package foobar.caching.caffeine

import foobar.caching.ICalculator
import mu.KotlinLogging
import org.springframework.cache.annotation.Cacheable
import javax.inject.Named
import kotlin.math.pow

@Named
class CalculatorCaffeineImpl : ICalculator {

  @Cacheable(value = ["areaOfCircleCaffeine"])
  override fun areaOfCircle(radius: Double): Double {
    logger.info("hit radius = {}", radius)
    return Math.PI * radius.pow(2)
  }

  private val logger = KotlinLogging.logger { }
}
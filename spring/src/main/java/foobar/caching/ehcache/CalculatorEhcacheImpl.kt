/**
 * Created by smallufo on 2020-05-20.
 */
package foobar.caching.ehcache

import foobar.caching.ICalculator
import mu.KotlinLogging
import org.springframework.cache.annotation.Cacheable
import javax.inject.Named
import kotlin.math.pow


@Named
open class CalculatorEhcacheImpl : ICalculator {

  @Cacheable(value = ["areaOfCircleEhcache"])
  //@Cacheable(cacheManager = "ehcacheCM")
  override fun areaOfCircle(radius: Double): Double {
    logger.info("hit radius = {}", radius)
    return Math.PI * radius.pow(2)
  }

  private val logger = KotlinLogging.logger { }
}

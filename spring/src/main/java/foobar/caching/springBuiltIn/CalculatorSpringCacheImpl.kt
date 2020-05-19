package foobar.caching.springBuiltIn

import foobar.caching.ICalculator
import mu.KotlinLogging
import org.springframework.cache.annotation.Cacheable
import javax.inject.Named
import kotlin.math.pow

@Named
open class CalculatorSpringCacheImpl : ICalculator {

  @Cacheable(cacheNames = ["areaOfCircle"])
  override fun areaOfCircle(radius: Double): Double {
    logger.info("hit radius = {}", radius)
    return Math.PI * radius.pow(2)
  }

  private val logger = KotlinLogging.logger { }
}

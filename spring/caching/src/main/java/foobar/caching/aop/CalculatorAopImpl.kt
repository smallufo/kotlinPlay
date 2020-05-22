/**
 * Created by smallufo on 2020-05-22.
 */
package foobar.caching.aop

import foobar.caching.ICalculator
import mu.KotlinLogging
import org.springframework.cache.annotation.Cacheable
import javax.inject.Named
import kotlin.math.pow


@Named
open class CalculatorAopImpl : ICalculator {

  override fun areaOfCircle(radius: Double): Double {
    return inner(radius)
  }

  @Cacheable("areaOfCircleAopCache")
  open fun inner(radius: Double) : Double {
    logger.info("hit radius = {}", radius)
    return Math.PI * radius.pow(2)
  }

  private val logger = KotlinLogging.logger { }
}

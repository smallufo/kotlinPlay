/**
 * Created by smallufo on 2020-05-22.
 */
package foobar.caching.ehcache

import foobar.caching.ICalculator
import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import java.lang.management.ManagementFactory
import javax.cache.management.CacheStatisticsMXBean
import javax.inject.Inject
import javax.management.MBeanServerInvocationHandler
import javax.management.ObjectInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [EhcacheConfig::class])
@OptIn(ExperimentalTime::class)
class CalculatorEhcacheImplWithStatTest {

  @Inject
  private lateinit var service: ICalculator

  private val logger = KotlinLogging.logger { }


  @Inject
  private lateinit var cacheBeans: Set<ObjectInstance>

  private val mxBean: CacheStatisticsMXBean by lazy {
    val filtered: ObjectInstance = cacheBeans.first {
      it.objectName.getKeyProperty("Cache") == "areaOfCircleEhcache"
    }
    val beanServer = ManagementFactory.getPlatformMBeanServer()
    MBeanServerInvocationHandler.newProxyInstance(beanServer, filtered.objectName, CacheStatisticsMXBean::class.java, false)
  }


  @Test
  fun testCaching() {

    val initGets = mxBean.cacheGets
    val initHits = mxBean.cacheHits

    assertEquals(initGets + 0, mxBean.cacheGets)
    assertEquals(initHits + 0, mxBean.cacheHits)

    val r1 = measureTimedValue { service.areaOfCircle(100.0) }
    assertEquals(initGets + 1, mxBean.cacheGets)
    assertEquals(initHits + 0, mxBean.cacheHits)

    val r2 = measureTimedValue { service.areaOfCircle(100.0) }
    assertEquals(initGets + 2, mxBean.cacheGets)
    assertEquals(initHits + 1, mxBean.cacheHits)

    logger.info("r1 = {} , r2 = {}", r1.duration, r2.duration)
    assertTrue(r2.duration < r1.duration)
    assertEquals(r1.value, r2.value)

  }
}

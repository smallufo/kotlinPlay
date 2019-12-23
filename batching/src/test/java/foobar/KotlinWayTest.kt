/**
 * Created by kevin.huang on 2019-12-12.
 */
package foobar

import mu.KotlinLogging
import kotlin.test.Test

class KotlinWayTest {

  private val logger = KotlinLogging.logger {  }

  @Test
  fun testParser() {
    val kImpl = KotlinWay()
    kImpl.reader().forEach { cityName ->
      logger.info("cityName : {}" , cityName)
    }
  }
}
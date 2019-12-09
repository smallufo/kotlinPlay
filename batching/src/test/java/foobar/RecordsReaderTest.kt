/**
 * Created by kevin.huang on 2019-12-09.
 */
package foobar

import mu.KotlinLogging
import kotlin.test.Test

class RecordsReaderTest {

  private val logger = KotlinLogging.logger {}

  @Test
  fun testRead() {
    val reader = RecordsReader()
    reader.read().forEach {
      logger.info("{}" , it)
    }
  }
}
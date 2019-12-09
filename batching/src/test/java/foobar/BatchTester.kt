/**
 * Created by kevin.huang on 2019-12-09.
 */
package foobar

import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import kotlin.test.Test

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(BatchConfig::class), loader = AnnotationConfigContextLoader::class)
class BatchTester {

  private val logger = KotlinLogging.logger { }

  @Autowired
  private lateinit var reader: RecordItemReader

  @Test
  fun testRun() {
    logger.info("reader = {}", reader)
  }
}
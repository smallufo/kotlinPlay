package foobar

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

/**
 * Created by kevin.huang on 2019-09-04.
 */
internal class LengthRuleTest {

  private val logger = KotlinLogging.logger { }
  private val rule = LengthRule(5, 12)

  @Test
  fun reason() {

    runBlocking {

      assertTrue(rule.passed("12345"))
      logger.info("passed")
    }
    logger.info("all passed")
  }



}
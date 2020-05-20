/**
 * Created by smallufo on 2020-03-12.
 */
package prop

import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.test.Test


@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [UserConfig::class])
class UserConfigTest {

  private val logger = KotlinLogging.logger { }

  @Inject
  private lateinit var user1: User

  @Inject
  @MyAnnotation(key = "K2", value = "V2")
  private lateinit var user2: User

  @Test
  fun printUser1() {
    logger.info("user1 = {}", user1)
    user1::class.memberProperties.forEach { prop: KProperty1<out User, *> ->
      prop.annotations.forEach { anno ->
        logger.info("user1 annotation = {}", anno)
      }
    }
  }

  @Test
  fun printUser2() {
    logger.info("user2 = {}", user2)
    this::class
      .memberProperties
      .first { it.name == "user2" }
      .annotations.forEach { anno ->
      logger.info("user2 annotation = {}", anno)
    }

  }
}

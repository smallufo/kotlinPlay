/**
 * Created by smallufo on 2020-05-15.
 */
package foobar.prop

import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals


@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [EmptyConfig::class])
class SpringAppInitTest {

  private val logger = KotlinLogging.logger { }

  @Inject
  private lateinit var applicationContext: ConfigurableApplicationContext

  private val springAppInit = SpringAppInit()


  @Test
  fun testCtx() {
    logger.info("configurableApplicationContext = {}", applicationContext)
    springAppInit.initialize(applicationContext)

    assertEquals("LocalUser", applicationContext.environment.getProperty("user.alias"))
  }


}

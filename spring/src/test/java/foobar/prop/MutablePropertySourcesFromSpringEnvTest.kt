/**
 * Created by smallufo on 2020-05-15.
 */
package foobar.prop

import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.TestPropertySources
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals


@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [EmptyConfig::class])
@TestPropertySources(
  //TestPropertySource("classpath:server-defaults.properties"),
  TestPropertySource("file:/tmp/server-local.properties")
)
class MutablePropertySourcesFromSpringEnvTest {

  @Inject
  private lateinit var applicationContext: ConfigurableApplicationContext

  private val logger = KotlinLogging.logger { }

  /**
   * ResourcePropertySource {name='URL [file:/tmp/server-local.properties]'}
   * ResourcePropertySource {name='class path resource [server-defaults.properties]'}
   * PropertiesPropertySource {name='systemProperties'}
  *  SystemEnvironmentPropertySource {name='systemEnvironment'}
   */
  @Test
  fun list() {
    applicationContext.environment.run {
      propertySources.forEach { pSource ->
        logger.info("{}", pSource)
      }

      assertEquals("LocalUser", getProperty("user.alias"))
    }
  }

}

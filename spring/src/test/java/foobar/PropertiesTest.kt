/**
 * Created by smallufo on 2020-05-13.
 */
package foobar

import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.ResourcePropertySource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.ResourceUtils
import java.util.*
import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals


@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [ServerConfig::class])
class PropertiesTest {

  @Inject
  private lateinit var applicationContext: ConfigurableApplicationContext

  private val logger = KotlinLogging.logger { }

  /**
   * user.alias=DefaultUser
   */
  @Test
  fun serverDefaults() {
    val userName = javaClass.getResourceAsStream("/server-defaults.properties").use { iStream ->
      Properties().apply {
        load(iStream)
      }["user.alias"]!!
    }
    assertEquals("DefaultUser", userName)
  }

  /**
   * server-defaults.properties : user.alias=DefaultUser
   * server-local.properties    : user.alias=LocalUser
   */
  @Test
  fun propertyOverride() {
    val propertySources = MutablePropertySources()
    propertySources.addLast(ResourcePropertySource("local", "classpath:server-local.properties"))
    propertySources.addLast(ResourcePropertySource("defaults", "classpath:server-defaults.properties"))
    propertySources.first().getProperty("user.alias").also {
      logger.info("user name = {}", it)
    }
  }

  @Test
  fun springConfig() {

    logger.info("applicationContext = {}", applicationContext::class)

    val localFile = ResourceUtils.getFile("classpath:server-local.properties")
    require(localFile.exists())


    logger.info("env = {} " , applicationContext.environment)


    applicationContext.environment.propertySources.forEach { propertySource: PropertySource<*> ->
      logger.info("{} , user.alias = {} , user.name = {}" , propertySource , propertySource.getProperty("user.alias") , propertySource.getProperty("user.name"))
    }

    /** by [org.springframework.core.env.PropertySourcesPropertyResolver] */
    assertEquals("LocalUser",applicationContext.environment.getProperty("user.alias"))

  }
}

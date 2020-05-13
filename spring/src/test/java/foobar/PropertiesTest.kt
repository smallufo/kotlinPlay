/**
 * Created by smallufo on 2020-05-13.
 */
package foobar

import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.PropertySourcesPropertyResolver
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.ResourcePropertySource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.ResourceUtils
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


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
  fun propertiesFrom_resource() {
    val propertySources = MutablePropertySources()
    propertySources.addLast(ResourcePropertySource("local", "classpath:server-local.properties"))
    propertySources.addLast(ResourcePropertySource("defaults", "classpath:server-defaults.properties"))

    propertySources.forEach { pSource ->
      logger.info("property source name = {} , user.alias = {}", pSource.name, pSource.getProperty("user.alias"))
    }
  }

  /**
   * /tmp/server-temp.properties : user.alias=TempUser
   */
  @Test
  fun propertiesFrom_map_file_classpath() {
    val propertySources = MutablePropertySources()

    propertySources.addLast(MapPropertySource("dynamic", mapOf("user.alias" to "DynamicUser")))
    propertySources.addLast(ResourcePropertySource(FileSystemResource(File("/tmp/server-temp.properties"))))
    propertySources.addLast(ResourcePropertySource(FileSystemResource(ResourceUtils.getFile("classpath:server-local.properties"))))
    propertySources.addLast(ResourcePropertySource(FileSystemResource(ResourceUtils.getFile("classpath:server-defaults.properties"))))
    propertySources.forEach { pSource ->
      logger.info("property source name = {} , user.alias = {}", pSource.name, pSource.getProperty("user.alias"))
    }

    val resolver = PropertySourcesPropertyResolver(propertySources)
    assertEquals("DynamicUser" , resolver.getProperty("user.alias"))
  }


  @Test
  fun reloadablePropertySource() {
    val propertySources = MutablePropertySources()

    propertySources.addLast(ReloadablePropertySource("SERVER-TEMP", FileSystemResource(File("/tmp/server-temp.properties"))))
    propertySources.addLast(ResourcePropertySource(FileSystemResource(ResourceUtils.getFile("classpath:server-defaults.properties"))))
    propertySources.forEach { pSource ->
      logger.info("property source name = {} , user.alias = {}", pSource, pSource.getProperty("user.alias"))
    }


  }


  /**
   * properties sequence :
   *    systemProperties
   *    systemEnvironment
   *    server-local.properties
   *    server-defaults.properties
   */
  @Test
  fun springConfig() {

//    logger.info("applicationContext = {}", applicationContext::class)
//    logger.info("env = {} ", applicationContext.environment)

    applicationContext.environment.propertySources.forEach { pSource ->
      logger.info("{} , user.alias = {} ", pSource.name, pSource.getProperty("user.alias"))
    }

    applicationContext.environment.also { env: ConfigurableEnvironment ->
      assertTrue(env is org.springframework.core.env.StandardEnvironment)

      /** default [org.springframework.core.env.PropertyResolver] by [org.springframework.core.env.PropertySourcesPropertyResolver] */
      assertTrue(env.containsProperty("user.alias"))
      assertFalse(env.containsProperty("XXX"))
      assertEquals("LocalUser", env.getProperty("user.alias"))
    }
  }


  @Test
  fun dumpSystemProperties() {
    applicationContext.environment.systemProperties.forEach { (k, v) ->
      logger.info("{} = {}", k, v)
    }
  }

  @Test
  fun dumpSystemEnvironment() {
    applicationContext.environment.systemEnvironment.forEach { (k, v) ->
      logger.info("{} = {}", k, v)
    }
  }
}

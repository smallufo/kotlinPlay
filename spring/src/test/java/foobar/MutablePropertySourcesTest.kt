/**
 * Created by smallufo on 2020-05-15.
 */
package foobar

import mu.KotlinLogging
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.PropertySourcesPropertyResolver
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.ResourcePropertySource
import org.springframework.util.ResourceUtils
import java.io.File
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals


class MutablePropertySourcesTest {

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
  fun propertiesFrom_file_classpath() {
    val propertySources = MutablePropertySources()
    propertySources.addLast(ResourcePropertySource(FileSystemResource(File("/tmp/server-local.properties"))))
    propertySources.addLast(ResourcePropertySource("defaults", "classpath:server-defaults.properties"))

    propertySources.forEach { pSource ->
      logger.info("property source name = {} , user.alias = {}", pSource.name, pSource.getProperty("user.alias"))
    }

    val resolver = PropertySourcesPropertyResolver(propertySources)
    assertEquals("LocalUser", resolver.getProperty("user.alias"))
  }

  /**
   * /tmp/server-temp.properties : user.alias=TempUser
   */
  @Test
  fun propertiesFrom_map_file_classpath() {
    val propertySources = MutablePropertySources()

    propertySources.addLast(MapPropertySource("dynamic", mapOf("user.alias" to "DynamicUser")))
    propertySources.addLast(ResourcePropertySource(FileSystemResource(File("/tmp/server-local.properties"))))
    propertySources.addLast(ResourcePropertySource(FileSystemResource(ResourceUtils.getFile("classpath:server-defaults.properties"))))
    propertySources.forEach { pSource ->
      logger.info("property source name = {} , user.alias = {}", pSource.name, pSource.getProperty("user.alias"))
    }

    val resolver = PropertySourcesPropertyResolver(propertySources)
    assertEquals("DynamicUser", resolver.getProperty("user.alias"))
  }

}

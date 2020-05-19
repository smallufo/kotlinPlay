/**
 * Created by smallufo on 2020-05-14.
 */
package foobar.prop

import mu.KotlinLogging
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.env.StandardEnvironment
import org.springframework.scheduling.annotation.Scheduled
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.annotation.PostConstruct
import javax.inject.Inject


abstract class ReloadableProperties {

  @Inject
  protected lateinit var environment: StandardEnvironment

  private var lastModTime = 0L

  private lateinit var configPath: Path
  private lateinit var appConfigPropertySource: PropertySource<*>


  @PostConstruct
  private fun stopIfProblemsCreatingContext() {
    logger.info("======== ======== ======== PostConstruct")
    val propertySources: MutablePropertySources = environment.propertySources.also { sources ->
      sources.forEach { ps ->
        logger.info("{}" , ps.name)
      }
    }



    appConfigPropertySource = propertySources.firstOrNull { ps ->
      logger.info("ps.name = '{}'" , ps.name)
      ps.name.matches("^file.*".toRegex())
      //ps.name.matches("^.*applicationConfig.*file:.*$".toRegex())
    } ?: {
      throw RuntimeException("Unable to find property Source as file")
    }.invoke()

    logger.info("appConfigPropertySource = {}" , appConfigPropertySource)

    val filename = appConfigPropertySource.name
      .replace("applicationConfig: [file:", "")
      .replace("\\]$".toRegex(), "")
    configPath = Paths.get(filename)
  }

  @Scheduled(fixedRate = 1000)
  private fun reload() {
    logger.info("======== ======== ======== Reloading")
    val currentModTs = Files.getLastModifiedTime(configPath).toMillis()
    if (currentModTs > lastModTime) {
      lastModTime = currentModTs
      val properties = Properties().apply {
        Files.newInputStream(configPath).use { iStream ->
          load(iStream)
        }
      }

      environment.propertySources.replace(appConfigPropertySource.name, PropertiesPropertySource(appConfigPropertySource.name, properties))

      logger.info("Property {} reloaded" , appConfigPropertySource.name)
      println("Reloaded.")
      propertiesReloaded()
    }
  }

  protected abstract fun propertiesReloaded()

  companion object {
    val logger = KotlinLogging.logger {  }
  }
}

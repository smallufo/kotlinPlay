/**
 * Created by smallufo on 2020-05-15.
 */
package foobar

import mu.KotlinLogging
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.ResourcePropertySource
import java.io.File


class SpringAppInit : ApplicationContextInitializer<ConfigurableApplicationContext> {

  override fun initialize(ctx: ConfigurableApplicationContext) {
    logger.info("============== SpringAppInit ")

    ctx.environment.propertySources.forEach { pSource ->
      logger.info("原有 {}" , pSource)
    }

    val localFile = File("/tmp/server-local.properties")
    require(localFile.exists())



    ctx.environment.propertySources.run {
      addLast(ReloadablePropertySource("local" , FileSystemResource(localFile)))
      addLast(ResourcePropertySource("defaults", "classpath:server-defaults.properties"))
    }

    logger.info("============== SpringAppInit (after modified)   ")

    ctx.environment.propertySources.forEach { pSource ->
      logger.info("最後 {}" , pSource)
    }
  }

  companion object {
    val logger = KotlinLogging.logger { }
  }
}

/**
 * Created by smallufo on 2020-05-15.
 */
package foobar

import mu.KotlinLogging
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.ResourcePropertySource
import org.springframework.util.ResourceUtils
import java.io.File


class SpringAppInit : ApplicationContextInitializer<ConfigurableApplicationContext> {

  override fun initialize(ctx: ConfigurableApplicationContext) {
    logger.info("============== SpringAppInit ")

    ctx.environment.propertySources.forEach { pSource ->
      logger.info("原有 {}" , pSource)
    }

    logger.info("{}", ctx.environment.propertySources)

    val tempFile = File("/tmp/server-local.properties")
    require(tempFile.exists())


    ctx.environment.propertySources.run {
      addLast(ResourcePropertySource(FileSystemResource(tempFile)))
      addLast(ResourcePropertySource(FileSystemResource(ResourceUtils.getFile("classpath:server-defaults.properties"))))
    }

    ctx.environment.propertySources.forEach { pSource ->
      logger.info("最後 {}" , pSource)
    }
  }

  companion object {
    val logger = KotlinLogging.logger { }
  }
}

/**
 * Created by kevin.huang on 2019-12-27.
 */
package hello

import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import java.util.*

@SpringBootApplication
class Application {

  private val logger = KotlinLogging.logger { }

  @Bean
  fun commandLineRunner(ctx: ApplicationContext): CommandLineRunner {
    return CommandLineRunner { args: Array<String> ->
      logger.info("commandLine")
      logger.info("Let's inspect the beans provided by Spring Boot:")
      val beanNames = ctx.beanDefinitionNames
      Arrays.sort(beanNames)
      for (beanName in beanNames) {
        println(beanName)
      }
    }
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      SpringApplication.run(Application::class.java, *args)
    }
  }
}
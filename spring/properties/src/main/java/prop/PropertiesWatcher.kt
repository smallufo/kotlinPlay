/**
 * Created by smallufo on 2020-05-15.
 */
package prop

import mu.KotlinLogging
import org.springframework.context.annotation.Lazy
import org.springframework.core.env.StandardEnvironment
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.inject.Inject


@Component
@Lazy(false)
class PropertiesWatcher {

  @Inject
  private lateinit var environment: StandardEnvironment

  @PostConstruct
  fun init() {
    environment.propertySources.filter { ps ->
      ps is ReloadablePropertySource
    }

  }

  companion object {
    val logger = KotlinLogging.logger {  }
  }
}

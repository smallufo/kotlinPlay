/**
 * Created by smallufo on 2020-05-13.
 */
package foobar

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources


@Configuration
@PropertySources(
  PropertySource("classpath:server-defaults.properties"),
  PropertySource("classpath:server-local.properties")
)
open class ServerConfig {



}

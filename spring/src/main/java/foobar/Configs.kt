/**
 * Created by smallufo on 2020-05-13.
 */
package foobar

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources


@Configuration
@PropertySources(
  PropertySource("classpath:server-defaults.properties"),
  PropertySource("file:/tmp/server-local.properties")
)
@ComponentScan(basePackages = ["foobar"])
open class ServerConfig

@Configuration
//@ComponentScan(basePackages = ["foobar"]) (不能 scan , 否則會掃到 server-defaults.properties )
open class EmptyConfig

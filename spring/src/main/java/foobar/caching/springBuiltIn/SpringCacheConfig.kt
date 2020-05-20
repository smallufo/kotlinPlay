/**
 * Created by smallufo on 2020-05-20.
 */
package foobar.caching.springBuiltIn

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@EnableCaching
@ComponentScan(basePackages = ["foobar.caching.springBuiltIn"])
class SpringCacheConfig {

  @Bean//(name = ["springCM"])
  fun cacheManager() : CacheManager {
    return ConcurrentMapCacheManager()
    //return ConcurrentMapCacheManager("areaOfCircle")
  }
}

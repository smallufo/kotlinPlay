/**
 * Created by kevin.huang on 2020-05-20.
 */
package foobar.caching.caffeine

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@EnableCaching
@ComponentScan(basePackages = ["foobar.caching.caffeine"])
class CaffeineCacheConfig {
  @Bean
  fun cacheManager(): CacheManager {
    return CaffeineCacheManager().apply {
      setCaffeine(Caffeine.newBuilder()
                    .initialCapacity(200)
                    .maximumSize(500)
                    .recordStats())
    }
  }
}
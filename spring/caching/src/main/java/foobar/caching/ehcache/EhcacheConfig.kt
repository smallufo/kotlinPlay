/**
 * Created by smallufo on 2020-05-20.
 */
package foobar.caching.ehcache

import mu.KotlinLogging
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import javax.cache.Cache
import javax.cache.CacheManager
import javax.cache.Caching
import javax.cache.spi.CachingProvider


@Configuration
@EnableCaching
@ComponentScan(basePackages = ["foobar.caching.ehcache"])
class EhcacheConfig {

  @Bean
  fun jCacheManager(): CacheManager {
    val provider: CachingProvider = Caching.getCachingProvider()
    logger.info("cachingProvider = {}", provider)

    val cacheManager: CacheManager = javaClass.getResource("/ehcache.xml").takeIf { it != null }?.let { ehcacheUrl ->
      logger.info("loading ehcache.xml from {}", ehcacheUrl)
      provider.getCacheManager(ehcacheUrl.toURI(), provider.defaultClassLoader)
    } ?: {
      logger.warn("No ehcache.xml found in classpath")
      provider.cacheManager
    }.invoke()

    for (s in cacheManager.cacheNames) {
      logger.info("[jCache] cacheName : {}", s)
    }

    return cacheManager
  }


  @Bean// (name = ["ehcacheCM"])
  fun cacheManager(jCacheManager: CacheManager): org.springframework.cache.CacheManager {
    return JCacheCacheManager(jCacheManager)
  }

  @Bean
  fun myCache(jCacheManager: CacheManager) : Cache<Any,Any> {
    return jCacheManager.getCache("myCache")
  }

  companion object {
    val logger = KotlinLogging.logger { }
  }
}

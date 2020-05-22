/**
 * Created by smallufo on 2020-05-22.
 */
package foobar.caching.aop

import mu.KotlinLogging
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.lang.management.ManagementFactory
import javax.cache.CacheManager
import javax.cache.Caching
import javax.cache.spi.CachingProvider
import javax.management.MBeanServer
import javax.management.ObjectInstance
import javax.management.ObjectName


@Configuration
@EnableCaching
@ComponentScan(basePackages = ["foobar.caching.aop"])
class EhcacheAopConfig {
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

  @Bean
  fun cacheManager(jCacheManager: CacheManager): org.springframework.cache.CacheManager {
    return JCacheCacheManager(jCacheManager)
  }

  @Bean
  fun cacheBeans(): Set<ObjectInstance> {
    val beanServer: MBeanServer = ManagementFactory.getPlatformMBeanServer()
    return beanServer.queryMBeans(ObjectName.getInstance("javax.cache:type=CacheStatistics,CacheManager=*,Cache=*"), null)
  }


  companion object {
    val logger = KotlinLogging.logger { }
  }
}

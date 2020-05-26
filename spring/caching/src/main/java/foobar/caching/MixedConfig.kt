/**
 * Created by kevin.huang on 2020-05-26.
 */
package foobar.caching

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@EnableCaching
@ComponentScan(basePackages = ["foobar.caching"])
class MixedConfig
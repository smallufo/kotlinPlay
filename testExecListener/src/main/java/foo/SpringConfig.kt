/**
 * Created by kevin.huang on 2020-06-02.
 */
package foo

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["foo"])
class SpringConfig {

}
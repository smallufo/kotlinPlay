/**
 * Created by kevin.huang on 2019-12-09.
 */
package foobar

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class BatchConfig {

  @Bean
  fun readStep(stepBuilderFactory: StepBuilderFactory, reader: ItemReader<Record>): Step {
    return stepBuilderFactory.get("step1").chunk<Record , Record>(10)
      .reader(reader)
      .build()
  }
}
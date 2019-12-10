/**
 * Created by kevin.huang on 2019-12-09.
 */
package foobar

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
@ComponentScan
open class BatchConfig {

  @Autowired
  private lateinit var reader: ItemReader<CityName>

  @Autowired
  private lateinit var processor: ItemProcessor<CityName, CityLocation>

  @Autowired
  private lateinit var writer: ItemWriter<CityLocation>

  @Bean
  open fun transactionManager(): PlatformTransactionManager {
    return ResourcelessTransactionManager()
  }

  @Bean
  open fun jobRepo(): JobRepository {
    return MapJobRepositoryFactoryBean(transactionManager()).`object`
  }

  @Bean
  open fun jobLauncher(): JobLauncher {
    return SimpleJobLauncher().apply {
      setJobRepository(jobRepo())
    }
  }

  @Bean
  open fun jobBuilderFactory(): JobBuilderFactory {
    return JobBuilderFactory(jobRepo())
  }

  @Bean
  open fun stepBuilderFactory(): StepBuilderFactory {
    return StepBuilderFactory(jobRepo(), transactionManager())
  }


  @Bean
  open fun step1(): Step {
    return stepBuilderFactory().get("readStep").chunk<CityName, CityLocation>(10)
      .reader(reader)
      .processor(processor)
      .writer(writer)
      .build()
  }

  @Bean
  open fun simpleJob() : Job {
    return jobBuilderFactory().get("simple").start(step1()).build()
  }


}
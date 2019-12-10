/**
 * Created by kevin.huang on 2019-12-09.
 */
package foobar

import mu.KotlinLogging
import org.junit.runner.RunWith
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.springframework.transaction.TransactionManager
import kotlin.test.Test

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [BatchConfig::class], loader = AnnotationConfigContextLoader::class)
class BatchTester {

  private val logger = KotlinLogging.logger { }


  @Autowired
  private lateinit var transactionManager: TransactionManager

  @Autowired
  private lateinit var jobRepo : JobRepository

  @Autowired
  private lateinit var jobLauncher : JobLauncher

  @Autowired
  private lateinit var jobBuilderFactory : JobBuilderFactory

  @Autowired
  private lateinit var stepBuilderFactory : StepBuilderFactory

  @Autowired
  private lateinit var step : Step

  @Test
  fun testRun() {
    logger.info("transactionManager = {}", transactionManager)
    logger.info("jobRepo = {}", jobRepo)
    logger.info("jobBuilderFactory = {}" , jobBuilderFactory)
    logger.info("jobLauncher = {}", jobLauncher)
    logger.info("stepBuilderFactory = {}" , stepBuilderFactory)
    logger.info("step = {}" , step)

    val job = jobBuilderFactory.get("simple").start(step).build()
    jobLauncher.run(job , JobParameters())
  }
}
package foobar

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

/**
 * Created by kevin.huang on 2019-09-04.
 */
internal class UserServiceTest {

  private val logger = KotlinLogging.logger { }

  private val userService = UserService()

  @ExperimentalCoroutinesApi
  @Test
  fun testUserProducer() {

    val t1 = measureTimeMillis {
      runBlocking {
        userService.userProducer().consumeEach { user ->
          logger.info("user = {}", user)
        }
      }
    }

    logger.info { "runBlocking takes $t1 millis" }

    val t2 = measureTimeMillis {
      GlobalScope.launch(Dispatchers.Unconfined) {
        userService.userProducer().consumeEach { user ->
          logger.info("user = {}", user)
        }
      }
    }
    logger.info { "global scope takes $t2 millis" }

    TimeUnit.MILLISECONDS.sleep(500)
  }

  @ExperimentalCoroutinesApi
  @Test
  fun testUserFlow() {
    runBlocking {
      val t0 = System.currentTimeMillis()
      val userFlow = userService.userFlow()
      val t1 = System.currentTimeMillis()
      logger.info { "get userFlow takes ${t1 - t0} millis" }

      userFlow
        .buffer(4)
        .collect {
          delay(100)

          println(it)
        }
      val t2 = System.currentTimeMillis()
      logger.info { "collect userFlow takes ${t2 - t1} millis" }
    }
  }

  @Test
  fun testUsernameFlow() {
    runBlocking {
      val t0 = System.currentTimeMillis()
      val usernameFlow = userService.usernameFlow()
      val t1 = System.currentTimeMillis()
      logger.info { "get usernameFlow takes ${t1 - t0} millis" }
      usernameFlow.collect { name ->
        logger.info("name = {}", name)
      }

      usernameFlow.collect { name ->
        logger.info("name2 = {}", name)
      }

    }
  }

}
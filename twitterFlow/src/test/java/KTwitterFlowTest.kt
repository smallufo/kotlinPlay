import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

/**
 * Created by kevin.huang on 2019-09-06.
 */
internal class KTwitterFlowTest {

  private val logger = KotlinLogging.logger { }

  @Test
  fun testFlow() {
    val flow = KTwitterFlow().flow


    runBlocking {
      flow.collect { status ->
        logger.info("[{}] {} : {}", status.id, status.user.name, status.text)
      }
    }

    TimeUnit.HOURS.sleep(1)
  }

  @Test
  fun testFlow2() {
    runBlocking {
      val flow: Flow<Int> = flow {
        for (i in 1..10) {
          emit(i)
        }
      }
      val flowEven: Flow<Int> = flow.filter { it % 2 == 0 }
      val flowOdd: Flow<Int> = flow.filter { it % 2 != 0 }

      flowOdd.collect { println(it) }
      flowEven.collect { println(it) }

    }
  }

  @Test
  fun coroutinesAreLightWeight() {
    runBlocking {
      repeat(1000) {
        launch(Dispatchers.Default) {
          delay(1000L)
          println("$it, ${Thread.currentThread().name}")

        }
      }
    }
  }
}
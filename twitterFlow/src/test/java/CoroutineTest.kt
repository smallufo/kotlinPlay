
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import kotlin.system.measureTimeMillis

/**
 * Created by kevin.huang on 2019-09-06.
 *
 * https://github.com/Kotlin/kotlinx.coroutines/blob/master/docs/composing-suspending-functions.md#sequential-by-default
 */

val logger = KotlinLogging.logger { }

fun main() = runBlocking {
  //sampleStart
  val time = measureTimeMillis {
    logger.info("The answer is ${concurrentSum()}")
  }
  println("Completed in $time ms")
  //sampleEnd
}

suspend fun concurrentSum(): Int = coroutineScope {
  val one = async { doSomethingUsefulOne() }
  val two = async { doSomethingUsefulTwo() }
  one.await() + two.await()
}

suspend fun doSomethingUsefulOne(): Int {
  delay(1000)
  //TimeUnit.MILLISECONDS.sleep(1000)
  return 13
}

suspend fun doSomethingUsefulTwo(): Int {
  delay(1000)
  //TimeUnit.MILLISECONDS.sleep(1000)
  return 29
}

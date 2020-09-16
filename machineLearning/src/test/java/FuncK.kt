import mu.KotlinLogging
import kotlin.test.Test

/**
 * Created by kevin.huang on 2020-08-31.
 */
class FuncK {

  private val logger = KotlinLogging.logger { }


  @Test
  fun testLazy() {
    val listA = (1..9).toList().asSequence()

    listA.map { i ->
      logger.info("calculating {}" , i)
      i * i
    }
      .filter { it % 2 == 0 }
      .forEach { logger.info("square = {}" , it) }
  }

  @Test
  fun testFlatMap() {
    val i = (1..9).toList().asSequence()
    val j = (1..9).toList().asSequence()
    val list99 = i.flatMap { a -> j.map { b ->
      val c = a * b
      logger.info("計算 {} * {} = {}" , a , b , c)
      Triple(a, b, c)
    } }
      //.filter { (a, _, _) -> a % 2 == 0 }


    list99.forEach { (a, b, c) ->
      logger.info("{} * {} = {}", a, b, c)
    }
  }
}
/**
 * Created by kevin.huang on 2019-11-12.
 */
package hedging

import kotlinx.coroutines.*

/**
 * https://stackoverflow.com/questions/58805372/
 */
fun main() {
  val eh = CoroutineExceptionHandler { _, e -> println("Handled by exception handler") }
  val context = eh + Job()

  CoroutineScope(context).launch {
    val res: Deferred<String> = async<String> { throw RuntimeException() }
    //val res: Deferred<String> = async<String>(context) { throw RuntimeException() }

    try {
      println("Result: ${res.await()}")
    } catch (e: Throwable) {

      println("Caught exception")
    }
  }


  Thread.sleep(1000)
}
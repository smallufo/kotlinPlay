package hedging

import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.Test
import ru.gildor.coroutines.okhttp.await
import java.net.URLEncoder

interface UrlShorter {
  suspend fun getShortUrl(longUrl: String): String?
}

val logger = KotlinLogging.logger {}

/** thread safe according to OkHttp document */
val okClient: OkHttpClient = OkHttpClient.Builder().build()

val ktorClient = io.ktor.client.HttpClient()

/**
Call.await() extension source :
https://github.com/gildor/kotlin-coroutines-okhttp/blob/master/src/main/kotlin/ru/gildor/coroutines/okhttp/CallAwait.kt
 */

class IsgdOkImpl : UrlShorter {
  override suspend fun getShortUrl(longUrl: String): String? {


    logger.info("running : {}", Thread.currentThread().name)
    val url = "https://is.gd/create.php?format=simple&url=%s".format(URLEncoder.encode(longUrl, "UTF-8"))

    val req = Request.Builder().url(url).build()

    return okClient.newCall(req).await().use { res ->
      try {
        res.body?.string().also {
          logger.info("returning {}", it)
        }
      } catch (e: Throwable) {
        null
      }
    }
  }
}

class IsgdKtorImpl : UrlShorter {
  override suspend fun getShortUrl(longUrl: String): String? {
    logger.info("running : {}", Thread.currentThread().name)
    val url = "https://is.gd/create.php?format=simple&url=%s".format(URLEncoder.encode(longUrl, "UTF-8"))

    return try {
      ktorClient.get<String>(url).also {
        logger.info("returning {}", it)
      }
    } catch (e: Throwable) {
      logger.warn("{}", e.message)
      null
    }
  }
}

class TinyOkImpl : UrlShorter {
  override suspend fun getShortUrl(longUrl: String): String? {
    logger.info("running : {}", Thread.currentThread().name)
    val url = "http://tinyurl.com/api-create.php?url=$longUrl"

    val req = Request.Builder().url(url).build()

    return okClient.newCall(req).await().use { res ->
      try {
        res.body?.string().also {
          logger.info("returning {}", it)
        }
      } catch (e: Throwable) {
        null
      }
    }
  }
}

class TinyKtorImpl : UrlShorter {
  override suspend fun getShortUrl(longUrl: String): String? {
    logger.info("running : {}", Thread.currentThread().name)
    val url = "http://tinyurl.com/api-create.php?url=$longUrl"

    return try {
      ktorClient.get<String>(url).also {
        logger.info("returning {}", it)
      }
    } catch (e: Throwable) {
      logger.warn("{}", e.message)
      null
    }
  }
}

/**
 * delays 10 seconds and returns null
 */
class DumbImpl : UrlShorter {
  override suspend fun getShortUrl(longUrl: String): String? {
    logger.info("running : {}", Thread.currentThread().name)
    delay(10 * 1000)
    return null
  }
}

/**
 * returns null immediately
 */
class NullImpl : UrlShorter {
  override suspend fun getShortUrl(longUrl: String): String? {
    logger.info("running : {}", Thread.currentThread().name)
    return null
  }
}


@ExperimentalCoroutinesApi
@FlowPreview
class UrlShorterService(private val impls: List<UrlShorter>) {

  suspend fun getShortUrl(longUrl: String): String {
    return methodFlowMerge1(longUrl)
  }


  /**
   * OK
   * 21:23:36.478 [main @coroutine#3] INFO  h.NullImpl.getShortUrl - running : main @coroutine#3
   * 21:23:36.479 [main @coroutine#4] INFO  h.DumbImpl.getShortUrl - running : main @coroutine#4
   * 21:23:36.485 [main @coroutine#5] INFO  h.IsgdKtorImpl.getShortUrl - running : main @coroutine#5
   * 21:23:36.853 [main @coroutine#6] INFO  h.TinyKtorImpl.getShortUrl - running : main @coroutine#6
   * 21:23:38.169 [main @coroutine#5] INFO  h.IsgdKtorImpl.getShortUrl - returning https://is.gd/EuvYes
   * 21:23:38.308 [main @coroutine#1] INFO  h.UrlShorterServiceTest$testHedging$1.invokeSuspend - result = https://is.gd/EuvYes
   *
   */
  private suspend fun methodFlowMerge1(longUrl: String): String {

    return impls.asFlow().flatMapMerge(impls.size) { impl ->
      flow<String?> {
        impl.getShortUrl(longUrl)?.also {
          emit(it)
        }
      }
    }.first() ?: longUrl
  }

  /**
   * OK
   *
   * 21:25:24.565 [main] INFO  h.UrlShorterServiceTest.testHedging - ktorClient.engine = io.ktor.client.engine.okhttp.OkHttpEngine@68c72235
   * 21:25:24.778 [DefaultDispatcher-worker-1 @coroutine#7] INFO  h.NullImpl.getShortUrl - running : DefaultDispatcher-worker-1 @coroutine#7
   * 21:25:24.786 [DefaultDispatcher-worker-2 @coroutine#8] INFO  h.DumbImpl.getShortUrl - running : DefaultDispatcher-worker-2 @coroutine#8
   * 21:25:24.791 [DefaultDispatcher-worker-3 @coroutine#9] INFO  h.IsgdKtorImpl.getShortUrl - running : DefaultDispatcher-worker-3 @coroutine#9
   * 21:25:24.792 [DefaultDispatcher-worker-6 @coroutine#10] INFO  h.TinyKtorImpl.getShortUrl - running : DefaultDispatcher-worker-6 @coroutine#10
   * 21:25:25.505 [DefaultDispatcher-worker-6 @coroutine#10] INFO  h.TinyKtorImpl.getShortUrl - returning http://tinyurl.com/389lo
   * 21:25:25.528 [main @coroutine#1] INFO  h.UrlShorterServiceTest$testHedging$1.invokeSuspend - result = http://tinyurl.com/389lo
   */
  private suspend fun methodFlowMerge2(longUrl: String): String {
    return impls.asSequence().asFlow().flatMapMerge(impls.size) { impl ->
      flow {
        impl.getShortUrl(longUrl)?.also {
          emit(it)
        }
      }.flowOn(Dispatchers.IO)
    }.first()
      .also { Dispatchers.IO.cancelChildren() }
  }


  /**
   * OK
   *
   * 12:57:22,663 INFO  destiny.hedging.NullImpl - running : main
   * 12:57:22,685 INFO  destiny.hedging.DumbImpl - running : main
   * 12:57:22,696 INFO  destiny.IsgdImpl - running : main
   * 12:57:22,846 INFO  destiny.hedging.TinyImpl - running : main
   * 12:57:23,296 INFO  destiny.hedging.UrlShorterServiceTest$testHedging$1 - result = http://tinyurl.com/389lo
   */
  private suspend fun methodChannel(longUrl: String): String {
    return coroutineScope {
      val chan = Channel<String?>()
      impls.forEach { impl ->
        launch {
          try {
            impl.getShortUrl(longUrl).also { chan.send(it) }
          } catch (e: Exception) {
            chan.send(null)
          }
        }
      }
      (1..impls.size).forEach { _ ->
        chan.receive()?.also { shortUrl ->
          coroutineContext[Job]!!.cancelChildren()
          return@coroutineScope shortUrl
        }
      }
      throw Exception("All services failed")
    }
  }

}


@ExperimentalCoroutinesApi
@FlowPreview
class UrlShorterServiceTest {

  @Test
  fun testHedging() {
    logger.info("ktorClient.engine = {}", ktorClient.engine)
    val impls = listOf(
      NullImpl()
      , DumbImpl()
      , IsgdKtorImpl()
      , TinyKtorImpl()
    )
    val service = UrlShorterService(impls)
    runBlocking {
      service.getShortUrl("https://www.google.com").also {
        logger.info("result = {}", it)
      }
    }
  }
}

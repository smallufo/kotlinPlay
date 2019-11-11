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
        res.body?.string()
      } catch (e: Throwable) {
        null
      }
    }
  }
}

class IsgdKtorImpl : UrlShorter {
  override suspend fun getShortUrl(longUrl: String): String? {
    logger.info("hedging.getKtorClient engine = {}", ktorClient.engine)
    logger.info("running : {}", Thread.currentThread().name)
    val url = "https://is.gd/create.php?format=simple&url=%s".format(URLEncoder.encode(longUrl, "UTF-8"))

    return ktorClient.get<String>(url)
  }
}

class TinyImpl : UrlShorter {
  override suspend fun getShortUrl(longUrl: String): String? {
    logger.info("running : {}", Thread.currentThread().name)
    val url = "http://tinyurl.com/api-create.php?url=$longUrl"

    val req = Request.Builder().url(url).build()

    return okClient.newCall(req).await().use { res ->
      try {
        res.body?.string()
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

    return ktorClient.get<String>(url)
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
   *
   * 12:55:32,617 INFO  destiny.hedging.NullImpl - running : main
   * 12:55:32,622 INFO  destiny.hedging.DumbImpl - running : main
   * 12:55:32,630 INFO  destiny.IsgdImpl - running : main
   * 12:55:32,711 INFO  destiny.hedging.TinyImpl - running : main
   * 12:55:33,150 INFO  destiny.hedging.UrlShorterServiceTest$testHedging$1 - result = http://tinyurl.com/389lo
   *
   */
  private suspend fun methodFlowMerge1(longUrl: String): String {
    return impls.asSequence().asFlow().flatMapMerge(impls.size) { impl ->
      flow {
        impl.getShortUrl(longUrl)?.also {
          emit(it)
        }
      }
    }.first()
  }

  /**
   * OK
   *
   * 12:53:36,227 INFO  hedging.TinyImpl - running : DefaultDispatcher-worker-5
   * 12:53:36,229 INFO  hedging.NullImpl - running : DefaultDispatcher-worker-1
   * 12:53:36,229 INFO  hedging.DumbImpl - running : DefaultDispatcher-worker-2
   * 12:53:36,227 INFO  IsgdImpl - running : DefaultDispatcher-worker-4
   * 12:53:37,135 INFO  hedging.UrlShorterServiceTest$testHedging$1 - result = http://tinyurl.com/389lo
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
    val impls = listOf(NullImpl(), DumbImpl(), IsgdOkImpl(), TinyImpl()
                       //, hedging.IsgdKtorImpl() , hedging.TinyKtorImpl()
                      )
    val service = UrlShorterService(impls)
    runBlocking {

      service.getShortUrl("https://www.google.com").also {
        logger.info("result = {}", it)
      }
    }
  }
}
/**
 * Created by kevin.huang on 2019-12-10.
 */
package foobar

import io.ktor.client.features.UserAgent
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import mu.KotlinLogging
import org.json.JSONObject

suspend fun <T> retry(
  times: Int = Int.MAX_VALUE,
  initialDelay: Long = 1000, // 1 second
  maxDelay: Long = 30000,   // 30 second
  factor: Double = 2.0,
  block: suspend () -> T): T {
  var currentDelay = initialDelay
  repeat(times - 1) {
    try {
      return block()
    } catch (e: Throwable) {
      KotlinLogging.logger { }.warn("throwable : {}", e.message)
    }
    delay(currentDelay)
    currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
  }
  return block() // last attempt
}


object Geocoder {

  private val ktorClient = io.ktor.client.HttpClient() {

    install(UserAgent) {
      agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Safari/605.1.15"
    }

    //BrowserUserAgent()
  }

  /**
   * sample : https://geocode.xyz/Taipei+Taiwan?json=1
  {
  "standard":{
  "addresst":{

  },
  "city":"Taipei",
  "prov":"TW",
  "countryname":"Taiwan",
  "postal":{

  },
  "confidence":"0.90"
  },
  "longt":"121.52925",
  "alt":{
  "loc":{
  "longt":"121.54307",
  "prov":"TW",
  "city":"Taipei",
  "countryname":"Taiwan",
  "postal":"106",
  "region":{

  },
  "latt":"25.03142"
  }
  },
  "elevation":{

  },
  "latt":"25.04246"
  }
   */
  suspend fun getLanLng(name: String, country: String): Pair<Double, Double>? {

    return retry {
      ktorClient.get<String>("https://geocode.xyz/$name $country?json=1").let { raw ->

        val json = JSONObject(raw)
        val lat = json.optNumber("latt", null)?.toDouble()
        val lng = json.optNumber("longt", null)?.toDouble()
        if (lat != null && lng != null) {
          (lat to lng).also {
            logger.info("querying : {} : {}  : {}", name, country, it)
          }
        } else {
          null
        }
      }
    }
  }
}
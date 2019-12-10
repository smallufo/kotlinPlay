/**
 * Created by kevin.huang on 2019-12-10.
 */
package foobar

import io.ktor.client.request.get
import org.json.JSONObject

object Geocoder {

  private val ktorClient = io.ktor.client.HttpClient()

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
    return ktorClient.get<String>("https://geocode.xyz/$name $country?json=1").let { raw ->

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
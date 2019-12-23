/**
 * Created by kevin.huang on 2019-12-12.
 */
package foobar

class KotlinWay {

  fun reader() : Sequence<CityName> {
    return javaClass.getResourceAsStream("/cities.csv")
      .bufferedReader()
      .lineSequence()
      .drop(1)
      .map(cityParser)

  }
}
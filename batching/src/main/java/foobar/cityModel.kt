/**
 * Created by kevin.huang on 2019-12-10.
 */
package foobar

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component


data class CityName(val name: String, val country: String)

data class CityLocation(val cityName: CityName, val lat: Double, val lng: Double)


val cityParser = { line: String ->
  val tokens = line.split(",")
  val name = tokens[0]
  val country = tokens[1]
  CityName(name, country)
}

val logger = KotlinLogging.logger { }

@Component
class CityItemReader : FlatFileItemReader<CityName>() {

  init {
    setLinesToSkip(1)
    setResource(ClassPathResource("cities.csv"))
    setLineMapper { line, index ->
      cityParser.invoke(line).also {
        logger.info("line {} : {}", index, it)
      }
    }
  }
}

@Component
class CityProcessor : ItemProcessor<CityName, CityLocation> {
  override fun process(city: CityName): CityLocation? {
    return runBlocking {
      delay(1500)
      Geocoder.getLanLng(city.name, city.country)?.let { (lat, lng) ->
        CityLocation(city, lat, lng)
      }
    }
  }
}

@Component
class CityWriter : ItemWriter<CityLocation> {
  override fun write(list: List<CityLocation>) {
    list.forEach { cityLoc ->
      logger.info("writing : {}", cityLoc)
    }
  }
}


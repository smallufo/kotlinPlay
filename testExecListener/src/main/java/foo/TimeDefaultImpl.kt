/**
 * Created by kevin.huang on 2020-06-02.
 */
package foo

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.pow


@Component
class TimeDefaultImpl : ITime {

  override fun getTime(timeZone: String): LocalDateTime {
    val zoneId = try {
      ZoneId.of(timeZone)
    } catch (ignored: Exception) {
      ZoneId.systemDefault()
    }

    return ZonedDateTime.now().withZoneSameInstant(zoneId).toLocalDateTime()
  }

}
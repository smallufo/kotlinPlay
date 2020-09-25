/**
 * Created by kevin.huang on 2020-06-02.
 */
package foo

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime


@Component
class TimeDefaultImpl : ITime {

  override fun getTime(timeZone: String, offsetMin: Long): LocalDateTime {
    val zoneId = try {
      ZoneId.of(timeZone)
    } catch (ignored: Exception) {
      ZoneId.systemDefault()
    }

    return ZonedDateTime.now().plusMinutes(offsetMin).withZoneSameInstant(zoneId).toLocalDateTime()
  }

}
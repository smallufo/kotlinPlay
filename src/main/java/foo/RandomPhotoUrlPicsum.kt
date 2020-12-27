/**
 * Created by smallufo on 2020-12-28.
 */
package foo

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*


class RandomPhotoUrlPicsum {

  /**
   * KTOR bug in 1.5.0
   * https://youtrack.jetbrains.com/issue/KTOR-1580
   */
  private val ktorClient = HttpClient {
    followRedirects = false
  }

  suspend fun getPhotoUrl(width: Int, height: Int): String? {

    val url = "https://picsum.photos/$width/$height"

    val statement: HttpStatement = ktorClient.get(url)
    val res = statement.execute()
    return res.headers.flattenEntries().firstOrNull { (name, _) -> name.equals("location", true) }
      ?.second
  }
}

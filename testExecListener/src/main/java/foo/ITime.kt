/**
 * Created by kevin.huang on 2020-06-02.
 */
package foo

import java.time.LocalDateTime


interface ITime {

  fun getTime(timeZone : String) : LocalDateTime
}


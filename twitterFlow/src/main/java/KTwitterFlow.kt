/**
 * Created by kevin.huang on 2019-09-06.
 */
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import twitter4j.*

class KTwitterFlow {
  private val logger = KotlinLogging.logger {}

  private val filter = FilterQuery().apply {
    track("trump", "Java", "Kotlin", "apple", "#iphone", "台灣", "台", "港", "中")
    //language("zh")
  }

  val flow: Flow<Status>

  init {
    flow = kotlinx.coroutines.flow.flow {
      val listener = object : StatusListener {
        override fun onStatus(status: Status) {
          GlobalScope.async {
            emit(status)
          }
        }

        override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
        override fun onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
        override fun onException(ex: Exception) {
          ex.printStackTrace()
        }

        override fun onStallWarning(warning: StallWarning?) {}
        override fun onScrubGeo(userId: Long, upToStatusId: Long) {}
      }
      TwitterStreamFactory().instance
        .addListener(listener)
        .filter(filter)


    }

  }


}
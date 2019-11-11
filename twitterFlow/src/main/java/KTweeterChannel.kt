import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import mu.KotlinLogging
import twitter4j.*
import kotlin.system.measureTimeMillis

/**
 * Created by kevin.huang on 2019-09-06.
 */
class KTweeterChannel {

  private val logger = KotlinLogging.logger {}

  private val filter = FilterQuery().apply {
    track("trump" , "Java", "Kotlin", "apple", "#iphone", "台灣", "台", "港", "中")
    language("zh")
  }


  val channel = Channel<Status>()

  init {

    val listener = object : StatusListener {
      override fun onStatus(status: Status) {

        val t0 = measureTimeMillis {
          val job = GlobalScope.launch {
            channel.send(status)
          }
          job.start()
        }
        logger.debug("send status , takes {} millis", t0)
      }

      override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
      override fun onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
      override fun onException(ex: Exception) { ex.printStackTrace() }
      override fun onStallWarning(warning: StallWarning?) {}
      override fun onScrubGeo(userId: Long, upToStatusId: Long) {}
    }

    TwitterStreamFactory().instance
      .addListener(listener)
      .filter(filter)
  }

}
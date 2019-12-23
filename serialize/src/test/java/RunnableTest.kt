import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import kotlinx.serialization.serializer
import mu.KotlinLogging
import kotlin.test.Test

class RunnableTest {

  private val logger = KotlinLogging.logger { }

  @ImplicitReflectionSerializer
  @Test
  fun testJson() {
    val module1 = serializersModuleOf(IRunnable::class, RunnableSerializer())

    Json(context = module1).stringify(IRunnable::class.serializer(), Horse()).also {
      logger.info("json = {}", it)
    }
  }
}
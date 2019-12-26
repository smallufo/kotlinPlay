
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import mu.KotlinLogging
import kotlin.test.Test
import kotlin.test.assertEquals


val logger = KotlinLogging.logger {  }

@Serializer(forClass = Horse::class)
object HorseSerializer {
}

@Serializer(forClass = Dog::class)
object DogSerializer {
}

@Serializable
data class Zoo(val runnable : IRunnable)



class RunnableTest {

  private val logger = KotlinLogging.logger { }

  @Test
  fun testJson() {

    val runnableModule = SerializersModule {
      polymorphic(IRunnable::class) {
        Horse::class with HorseSerializer
        Dog::class with DogSerializer
      }
    }

    val zoo = Zoo(Dog())

    val json = Json(context = runnableModule)

    json.stringify(Zoo.serializer() , zoo).also {
      logger.info("zoo = {}" , it)
      json.parse(Zoo.serializer() , it).also { z ->
        logger.info("{}" , z)
        assertEquals(zoo , z)
      }
    }
  }
}

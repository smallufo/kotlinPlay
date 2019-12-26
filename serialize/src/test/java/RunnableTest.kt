import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.serializersModuleOf
import mu.KotlinLogging
import kotlin.test.Test
import kotlin.test.assertEquals


val logger = KotlinLogging.logger {  }


@Serializer(forClass = Cat::class)
object CatSerializer

@Serializer(forClass = Dog::class)
object DogSerializer

@Serializable
data class Zoo(val runnable : IRunnable)

class RunnableTest {

  private val logger = KotlinLogging.logger { }

  @Test
  fun testJson() {

    val runnableModule = SerializersModule {
      polymorphic(IRunnable::class) {
        Cat::class with CatSerializer
        Dog::class with DogSerializer
      }
    }

    val module2 = serializersModuleOf(IRunnable::class , RunnableSerializer())

    val zoo = Zoo(Dog())

    //val json = Json(context = runnableModule)
    val json = Json(context = module2)

    json.stringify(Zoo.serializer() , zoo).also {
      logger.info("zoo = {}" , it)
      json.parse(Zoo.serializer() , it).also { z ->
        logger.info("{}" , z)
        assertEquals(zoo , z)
      }
    }
  }
}

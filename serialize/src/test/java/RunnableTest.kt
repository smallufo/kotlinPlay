import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.serializersModule
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

    val module1 = SerializersModule {
      polymorphic(IRunnable::class) {
        Cat::class with CatSerializer
        Dog::class with DogSerializer
      }
    }

    val module2 = serializersModule(RunnableSerializer())
    val module3 = serializersModuleOf(IRunnable::class , RunnableSerializer())

    val zoo = Zoo(Dog())

    val json = Json(context = module3)

    json.stringify(Zoo.serializer() , zoo).also {
      logger.info("zoo = {}" , it)
      json.parse(Zoo.serializer() , it).also { z ->
        logger.info("{}" , z)
        assertEquals(zoo , z)
      }
    }
  }
}

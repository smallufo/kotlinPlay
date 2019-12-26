import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.modules.SerializersModule
import mu.KotlinLogging
import kotlin.test.Test
import kotlin.test.assertEquals


val logger = KotlinLogging.logger {  }


@Serializer(forClass = Horse::class)
object HorseSerializer {
  override fun serialize(encoder: Encoder, obj: Horse) {
    encoder.encodeString("H")
  }
}

@Serializer(forClass = Dog::class)
object DogSerializer {
//  override fun serialize(encoder: Encoder, obj: Dog) {
//    encoder.encodeString("D")
//  }
//
//  override fun deserialize(decoder: Decoder): Dog {
//    val input = decoder as? JsonInput
//      ?: throw SerializationException("This class can be loaded only by Json")
//    val tree = input.decodeJson() as? JsonObject
//      ?: throw SerializationException("Expected JsonObject")
//    logger.info("tree = {}" , tree)
//    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//  }
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

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import mu.KotlinLogging
import kotlin.test.Test
import kotlin.test.assertTrue


val logger = KotlinLogging.logger { }


@Serializer(forClass = Cat::class)
object CatSerializer {
  override val descriptor: SerialDescriptor
    get() = buildClassSerialDescriptor("CAT")

  override fun serialize(encoder: Encoder, value: Cat) {
    encoder.encodeString("CAT")
  }

  override fun deserialize(decoder: Decoder): Cat {
    return Cat()
  }
}


@Serializable
data class Zoo(
  val animal: Animal
)

class RunnableTest {

  private val logger = KotlinLogging.logger { }

  @Test
  fun testJson() {

//    val module1 = SerializersModule {
//      polymorphic(IRunnable::class) {
//        Cat::class with CatSerializer
//        Dog::class with DogSerializer
//      }
//    }

    val module2 = serializersModuleOf(RunnableSerializer())
    val module3 = serializersModuleOf(IRunnable::class, RunnableSerializer())

//    val module4 = SerializersModule {
//      polymorphic(Animal::class) {
//        Cat::class with Cat.serializer()
//        Dog::class with Dog.serializer()
//      }
//    }
//
//    val module5 = SerializersModule {
//      polymorphic(Animal::class) {
//        Cat::class with CatSerializer
//        Dog::class with DogSerializer
//      }
//    }
//
//    val module6 = SerializersModule {
//      polymorphic(Animal::class) {
//        addSubclass(CatSerializer)
//        addSubclass(DogSerializer)
//      }
//    }

    val zoo = Zoo(Dog())

    val json = Json { serializersModule = module3 }

    json.encodeToString(Zoo.serializer(), zoo).also {
      logger.info("zoo = {}", it)
      json.decodeFromString(Zoo.serializer(), it).also { z ->
        logger.info("{}", z)
        assertTrue { z.animal is Dog }
        //assertEquals(zoo , z)
      }
    }
  }
}

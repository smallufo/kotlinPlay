import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.serializersModule
import kotlinx.serialization.modules.serializersModuleOf
import mu.KotlinLogging
import kotlin.test.Test
import kotlin.test.assertTrue


val logger = KotlinLogging.logger { }


@Serializer(forClass = Cat::class)
object CatSerializer {
  override val descriptor: SerialDescriptor
    get() = SerialClassDescImpl("CAT")

  override fun serialize(encoder: Encoder, obj: Cat) {
    encoder.encodeString("CAT")
  }

  override fun deserialize(decoder: Decoder): Cat {
    return Cat()
  }
}

@Serializer(forClass = Dog::class)
object DogSerializer {
//  override val descriptor: SerialDescriptor
//    get() = SerialClassDescImpl("DOG")
//    //get() = StringDescriptor.withName("DOG")
//
//  override fun serialize(encoder: Encoder, obj: Dog) {
//    encoder.encodeString("DOG")
//  }
//
//  override fun deserialize(decoder: Decoder): Dog {
//    return Dog()
//  }

}

@Serializable
data class Zoo(
  val animal: Animal
)

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
    val module3 = serializersModuleOf(IRunnable::class, RunnableSerializer())

    val module4 = SerializersModule {
      polymorphic(Animal::class) {
        Cat::class with Cat.serializer()
        Dog::class with Dog.serializer()
      }
    }

    val module5 = SerializersModule {
      polymorphic(Animal::class) {
        Cat::class with CatSerializer
        Dog::class with DogSerializer
      }
    }

    val module6 = SerializersModule {
      polymorphic(Animal::class) {
        addSubclass(CatSerializer)
        addSubclass(DogSerializer)
      }
    }

    val zoo = Zoo(Dog())

    //val json = Json {}
    val json = Json(context = module6)
    //val json = Json { serialModule = module5 }

    json.stringify(Zoo.serializer(), zoo).also {
      logger.info("zoo = {}", it)
      json.parse(Zoo.serializer(), it).also { z ->
        logger.info("{}", z)
        assertTrue { z.animal is Dog }
        //assertEquals(zoo , z)
      }
    }
  }
}


import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonObject

interface IRunnable {
  fun run()
}

@Polymorphic
@Serializable
abstract class Animal

@Serializable
class Cat : Animal() , IRunnable {
  override fun run() {
    println("horse running")
  }
}

@Serializable
class Dog : Animal() , IRunnable {
  override fun run() {
    println("dog running")
  }
}


/**
 * https://medium.com/transferwise-engineering/how-to-master-polymorphism-and-custom-serializers-in-kotlinx-serialization-7190da0f42aa
 */
class AnimalSerializer : KSerializer<Animal> {
  override val descriptor: SerialDescriptor
    get() = StringDescriptor.withName("animal")


  override fun serialize(encoder: Encoder, obj: Animal) {
    when(obj) {
      is Cat -> encoder.encodeString("C")
      is Dog -> encoder.encodeString("D")
    }
  }

  override fun deserialize(decoder: Decoder): Animal {
    val input = decoder as? JsonInput ?: throw SerializationException("Expected JsonInput for ${decoder::class}")
    val jsonObject = input.decodeJson() as? JsonObject ?: throw SerializationException("Expected JsonObject for ${input.decodeJson()::class}")
    println("jsonObject = $jsonObject")
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}


@Serializer(forClass = IRunnable::class)
class RunnableSerializer : KSerializer<IRunnable> {
  override val descriptor: SerialDescriptor
    get() = SerialClassDescImpl("runnable")
    //get() = StringDescriptor.withName("runnable")

  override fun serialize(encoder: Encoder, obj: IRunnable) {
    val stringValue = when (obj) {
      is Cat -> { "C" }
      is Dog -> { "D" }
      else -> { null }
    }
    stringValue?.also {
      encoder.encodeString(it)
    }
  }

  override fun deserialize(decoder: Decoder): IRunnable {
    return decoder.decodeString().let { value ->
      when(value) {
        "C" -> Cat()
        "D" -> Dog()
        else -> throw RuntimeException("invalid $value")
      }
    }
  }
}

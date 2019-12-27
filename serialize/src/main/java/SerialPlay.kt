import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.internal.StringDescriptor

interface IRunnable {
  fun run()
}

class Cat : IRunnable {
  override fun run() {
    println("horse running")
  }
}

class Dog : IRunnable {
  override fun run() {
    println("dog running")
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

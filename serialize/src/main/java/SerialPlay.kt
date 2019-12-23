import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

interface IRunnable {
  fun run()
}

class Horse : IRunnable {
  override fun run() {
    println("horse running")
  }
}

class Dog : IRunnable {
  override fun run() {
    println("dog running")
  }
}

class RunnableSerializer : KSerializer<IRunnable> {
  override val descriptor: SerialDescriptor
    get() = StringDescriptor.withName("runnable")



  override fun serialize(encoder: Encoder, obj: IRunnable) {
    val stringValue = when (obj) {
      is Horse -> { "H" }
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
        "H" -> Horse()
        "D" -> Dog()
        else -> throw RuntimeException("invalid $value")
      }
    }
  }
}
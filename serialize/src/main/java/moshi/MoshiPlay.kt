package moshi

import com.squareup.moshi.*


interface IRunnable {
  fun run()
}

class Cat : IRunnable {
  override fun run() { println("cat running") }
}

class Dog : IRunnable {
  override fun run() { println("dog running") }
}

interface IMapConverter<T> {
  fun getMap(impl : T) : Map<String , String>
  fun getImpl(map : Map<String , String>) : T?
}

class RunnableConverter : IMapConverter<IRunnable> {
  private val key = "runnable"

  override fun getMap(impl: IRunnable): Map<String, String> {
    val value = when(impl) {
      is Cat -> "C"
      is Dog -> "D"
      else -> throw RuntimeException("error")
    }
    return mapOf(key to value)
  }

  override fun getImpl(map: Map<String, String>): IRunnable? {
    return map[key]?.let { value ->
      when (value) {
        "C" -> Cat()
        "D" -> Dog()
        else -> throw RuntimeException("error")
      }
    }
  }

  fun getAdapter() : JsonAdapter<IRunnable> {
    return object : JsonAdapter<IRunnable>() {

      @ToJson
      override fun toJson(writer: JsonWriter, runnable: IRunnable?) {
        runnable?.also { impl ->
          writer.beginObject()
          getMap(impl).forEach { (key , value) ->
            writer.name(key).value(value)
          }
          writer.endObject()
        }
      }

      @FromJson
      override fun fromJson(reader: JsonReader): IRunnable? {
        reader.beginObject()

        val map = mutableMapOf<String , String>().apply {
          while (reader.hasNext()) {
            put(reader.nextName() , reader.nextString())
          }
        }
        val result = getImpl(map)

        reader.endObject()
        return result
      }
    }
  }
}

inline fun <reified T> IMapConverter<T>.toJsonAdapter() : JsonAdapter<T> {
  return object : JsonAdapter<T>() {

    @ToJson
    override fun toJson(writer: JsonWriter, value: T?) {
      value?.also { impl ->
        writer.beginObject()
        getMap(impl).forEach { (key , value) ->
          writer.name(key).value(value)
        }
        writer.endObject()
      }
    }

    @FromJson
    override fun fromJson(reader: JsonReader): T? {
      reader.beginObject()

      val map = mutableMapOf<String , String>().apply {
        while (reader.hasNext()) {
          put(reader.nextName() , reader.nextString())
        }
      }

      val result = getImpl(map)
      reader.endObject()
      return result
    }
  }
}


/**
 * Created by smallufo on 2019-12-29.
 */
package moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import mu.KotlinLogging


interface IRunnable {
  fun run()
}

class Cat : IRunnable {
  override fun run() {
    println("cat running")
  }
}

class Dog : IRunnable {
  override fun run() {
    println("dog running")
  }
}

@JsonClass(generateAdapter = true)
data class Zoo(
  val runnable : IRunnable
)

class RunnableAdapter {

  @ToJson
  fun toJson(runnable: IRunnable) : String {
    return when (runnable) {
      is Cat -> {
        "C"
      }
      is Dog -> {
        "D"
      }
      else -> {
        "X"
      }
    }
  }

  @FromJson
  fun fromJson(raw : String): IRunnable {
    return if (raw == "C") {
      Cat()
    }  else if (raw =="D") {
      Dog()
    } else {
      throw RuntimeException("?")
    }
  }
}

fun main() {
  val logger = KotlinLogging.logger {  }
  val moshi = Moshi.Builder()
    .add(RunnableAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()


  val ada = moshi.adapter<IRunnable>(IRunnable::class.java)
  ada.toJson(Dog()).also { json ->
    logger.info("json of dog = {}" , json)
    ada.fromJson(json).also { runnable ->
      logger.info("runnable = {}" , runnable)
    }
  }

  val zoo = Zoo(Dog())

  val adapter = moshi.adapter<Zoo>(Zoo::class.java)
  adapter.toJson(zoo).also { json ->
    logger.info("zoo json = {}" , json)
    adapter.fromJson(json).also { z ->
      logger.info("z obj = {}" , z)
    }
  }
}

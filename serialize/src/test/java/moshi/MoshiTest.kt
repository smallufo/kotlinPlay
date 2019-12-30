package moshi

import com.squareup.moshi.Moshi
import mu.KotlinLogging
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoshiTest {

  private val logger = KotlinLogging.logger { }

  @Test
  fun testConverter1() {
    val converter = RunnableConverter()

    val moshi = Moshi.Builder()
      .add(converter.getAdapter())
      .build()

    val adapter = moshi.adapter<IRunnable>(IRunnable::class.java)
    adapter.toJson(Dog()).also { json ->
      assertEquals("""{"runnable":"D"}""" , json)
      adapter.fromJson(json).also { runnable ->
        assertTrue(runnable is Dog)
      }
    }
  }


  @Test
  fun testConverter2() {
    val converter = RunnableConverter()

    val moshi = Moshi.Builder()
      .add(converter.toJsonAdapter())
      .build()

    val adapter = moshi.adapter<IRunnable>(IRunnable::class.java)
    adapter.toJson(Dog()).also { json ->
      logger.info("json of dog = {}", json)
      assertEquals("""{"runnable":"D"}""" , json)
      adapter.fromJson(json).also { runnable ->
        assertTrue(runnable is Dog)
      }
    }
  }

  @Test
  fun testConverter3() {
    val converter = RunnableConverter()

    val moshi = Moshi.Builder()
      .add(converter.toJsonAdapter())
      .build()

    val adapter = moshi.adapter<Any>(Any::class.java)
    adapter.toJson(Dog()).also { json ->
      logger.info("json of dog = {}", json)
      assertEquals("""{"runnable":"D"}""" , json)
      adapter.fromJson(json).also { runnable ->
        assertTrue(runnable is Dog)
      }
    }
  }
}
/**
 * Created by smallufo on 2023-10-24.
 */

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import mu.KotlinLogging
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class TimePlaceSerializerTest {

    private val logger = KotlinLogging.logger { }

    private val json1 = Json {
        encodeDefaults = false
    }

    private val json2 = Json {
        serializersModule = SerializersModule {
            contextual(LocalDateTime::class, LocalDateTimeSerializer)
        }
        encodeDefaults = false
    }

    @Test
    fun test1() {
        val time = LocalDateTime.of(2023, 10, 24, 1, 50)
        val place = "New York"
        val timePlace = TimePlace(time, place)

        val serialized = json1.encodeToString(TimePlaceSerializer, timePlace)
        logger.info { serialized }
        val deserialized = json1.decodeFromString(TimePlaceSerializer, serialized)

        assertEquals(timePlace, deserialized)
    }

    @Test
    fun test2() {
        val time = LocalDateTime.of(2023, 10, 24, 1, 50)
        val place = "New York"
        val timePlace = TimePlace(time, place)

        val serialized = json2.encodeToString(TimePlaceSerializer, timePlace)
        logger.info { serialized }
        val deserialized = json2.decodeFromString(TimePlaceSerializer, serialized)

        assertEquals(timePlace, deserialized)
    }
}

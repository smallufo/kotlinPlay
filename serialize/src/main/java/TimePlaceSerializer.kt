/**
 * Created by smallufo on 2023-10-24.
 */
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import java.time.LocalDateTime

object TimePlaceSerializer : KSerializer<TimePlace> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("TimePlace") {
        element<LocalDateTime>("time")
        element<String>("place")
    }

    override fun serialize(encoder: Encoder, value: TimePlace) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, LocalDateTimeSerializer, value.time)
            encodeStringElement(descriptor, 1, value.place)
        }
    }

    override fun deserialize(decoder: Decoder): TimePlace {
        var time = LocalDateTime.now()
        var place = ""
        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (decodeElementIndex(descriptor)) {
                    0 -> time = decodeSerializableElement(descriptor, 0, LocalDateTimeSerializer)
                    1 -> place = decodeStringElement(descriptor, 1)
                    else -> break@loop
                }
            }
        }
        return TimePlace(time, place)
    }
}

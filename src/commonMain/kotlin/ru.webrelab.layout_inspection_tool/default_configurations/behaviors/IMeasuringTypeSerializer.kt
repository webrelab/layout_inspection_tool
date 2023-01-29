package ru.webrelab.layout_inspection_tool.default_configurations.behaviors

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = IMeasuringType::class)
class IMeasuringTypeSerializer : KSerializer<IMeasuringType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("IMeasuringType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: IMeasuringType) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): IMeasuringType {
        val name = decoder.decodeString()
        return LitConfig.config<Any>().measuringTypes.first { it.toString() == name }
    }
}
package com.nordokod.scio.kt.utils

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

object DateSerializer : KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)
    override fun deserialize(decoder: Decoder): Date {
        return  Date(decoder.decodeLong())
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time)
    }


}
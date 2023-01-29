package ru.webrelab.layout_inspection_tool.ifaces

import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.IMeasuringTypeSerializer

@Serializable(with = IMeasuringTypeSerializer::class)
interface IMeasuringType {
    val id : String
    val color: String
    val deg: Int
    val isComplex: Boolean
    val isPositionOnly: Boolean
    fun <E> create(element: E): IRepository
}
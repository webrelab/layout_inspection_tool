package ru.webrelab.layout_inspection_tool

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import ru.webrelab.layout_inspection_tool.ifaces.IElement
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType
import ru.webrelab.layout_inspection_tool.ifaces.IRepository
import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.Size

@Serializable
class LayoutElement<E>(
    override val name: String,
    override var ignore: Boolean,
    override val type: IMeasuringType,
    override val position: Position,
    override val size: Size,
    override var container: Position,
    override val data: IRepository,
    @Transient override val element: E? = null,
    val transform: String
) : IElement<E> {
    override val id: String = IElement.generateId()
    companion object {
        fun <E> decode(json: String): LayoutElement<E> = Json.decodeFromString(json)
    }
}
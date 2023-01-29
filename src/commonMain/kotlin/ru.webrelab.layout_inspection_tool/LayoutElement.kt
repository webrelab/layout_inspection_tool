package ru.webrelab.layout_inspection_tool

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.ifaces.IElement
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType
import ru.webrelab.layout_inspection_tool.ifaces.IRepository
import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.Size

@Serializable
class LayoutElement(
    override val name: String,
    override var ignore: Boolean,
    override val type: IMeasuringType,
    override val position: Position,
    override val size: Size,
    override var container: Position,
    @Polymorphic
    override val data: IRepository,
    val transform: String
) : IElement {
    override val id: String = IElement.generateId("$type-$name")
    override fun toString(): String {
        return "$id; Size: ${size.width} x ${size.height}; Position: ${position.left} : ${position.top}"
    }
}
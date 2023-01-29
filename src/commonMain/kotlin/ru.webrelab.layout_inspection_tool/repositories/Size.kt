package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.ifaces.IHasFault
import ru.webrelab.layout_inspection_tool.screen_difference.Difference

@Serializable
data class Size(val width: Int, val height: Int) : IHasFault {
    override fun getDifferences(actual: IHasFault): List<Difference> = listOfNotNull(
        getDifference("width", x(), actual.x()),
        getDifference("height", y(), actual.y())
    )

    override fun x(): Int {
        return width
    }

    override fun y(): Int {
        return height
    }

    fun isEmpty(): Boolean = width == 0 || height == 0
}

package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.ifaces.IHasFault
import ru.webrelab.layout_inspection_tool.screen_difference.Difference

@Serializable
class Position(val left: Int, val top: Int) : IHasFault {
    override fun getDifferences(actual: IHasFault): List<Difference> = listOfNotNull(
        getDifference("left", x(), actual.x()),
        getDifference("top", y(), actual.y())
    )

    override fun x(): Int {
        return left
    }

    override fun y(): Int {
        return top
    }

    fun getRelativePosition(container: Position) = Position(left - container.left, top - container.top)
}

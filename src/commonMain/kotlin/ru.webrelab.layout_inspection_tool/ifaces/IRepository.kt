package ru.webrelab.layout_inspection_tool.ifaces

import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.Size
import ru.webrelab.layout_inspection_tool.screen_difference.Difference

interface IRepository {
    fun dataMap(): Map<String, Any>
    fun compare(actual: IRepository): List<Difference>
    fun getSize(): Size
    fun getPosition(): Position
    fun getRelativePosition(container: Position): Position
}
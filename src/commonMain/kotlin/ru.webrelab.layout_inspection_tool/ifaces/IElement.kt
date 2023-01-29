package ru.webrelab.layout_inspection_tool.ifaces

import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.Size

interface IElement {
    companion object {
        private var id = mutableMapOf<String, Int> ()
        fun generateId(type: String): String {
            if (!id.containsKey(type)) id[type] = 0
            id[type] = id[type]!! + 1
            return "$type-${id[type]}"
        }
    }
    val id: String
    val name: String
    val ignore: Boolean
    val type: IMeasuringType
    val position: Position
    val size: Size
    var container: Position
    val data: IRepository
}
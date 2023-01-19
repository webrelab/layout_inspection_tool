package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.ifaces.IHasFault

@Serializable
data class Size(val width: Int, val height: Int) : IHasFault {
    override fun x(): Int {
        return width
    }

    override fun y(): Int {
        return height
    }

    fun isEmpty(): Boolean = width == 0 || height == 0
}

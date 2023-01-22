package ru.webrelab.layout_inspection_tool.ifaces

import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.Size
import kotlin.jvm.Transient

interface IElement<E> {
    companion object {
        private var id: Int = 1;
        fun generateId(): String {
            return "${id++}"
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
    val element: E?
}
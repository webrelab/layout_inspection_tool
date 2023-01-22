package ru.webrelab.layout_inspection_tool.ifaces

import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.Size
import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault

interface IAdapter<E> {
    fun findElements(request: String): Collection<E>
    fun findElements(element: E, request: String): Collection<E>
    fun executeJsScript(js: String, vararg element: Any): Any?
    fun getPosition(element: E): Position
    fun getWindowSize(): Size
    fun setWindowSize(size: Size)
}
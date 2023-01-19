package ru.webrelab.layout_inspection_tool.ifaces

import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault

interface IDrawer<E> {
    fun isDrawPossible(): Boolean
    fun drawCanvas()
    fun drawElement(element: IElement<E>, vararg color: String)
    fun drawElements(elements: Collection<IElement<E>>, vararg color: String) {
        elements.forEach { drawElement(it, *color) }
    }
    fun drawComparisonFault(comparisonFaultList: Collection<ComparisonFault<E>>)
}
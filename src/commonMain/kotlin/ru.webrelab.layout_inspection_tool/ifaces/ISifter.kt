package ru.webrelab.layout_inspection_tool.ifaces

import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault

interface ISifter<E> {
    val violation: Int
    fun combine(expected: Map<String, IElement>, actual: Map<String, IElement>)
    fun sift()
    fun getFaultList(): List<ComparisonFault<E>>
}
package ru.webrelab.layout_inspection_tool.screen_difference

import ru.webrelab.layout_inspection_tool.ifaces.IElement

class ComparisonFault<E>(
    private val expected: IElement<E>?,
    private val actual: IElement<E>?,
    private val diff: List<Difference>?
) {
    fun hasDiff() = diff == null
    fun isExpectedAbsent() = expected == null
    fun isActualAbsent() = actual == null
    fun getActual(): IElement<E>? = actual
    fun getExpected(): IElement<E>? = expected
    fun getDiff(): List<Difference>? = diff
}
package ru.webrelab.layout_inspection_tool.screen_difference

import ru.webrelab.layout_inspection_tool.ifaces.IElement

class ComparisonFault<E>(
    private val expected: IElement?,
    private val actual: IElement?,
    private val diff: List<Difference>?
) {
    fun hasDiff() = !diff.isNullOrEmpty()
    fun isExpectedAbsent() = expected == null
    fun isActualAbsent() = actual == null
    fun getActual(): IElement? = actual
    fun getExpected(): IElement? = expected
    fun getDiff(): List<Difference>? = diff
}
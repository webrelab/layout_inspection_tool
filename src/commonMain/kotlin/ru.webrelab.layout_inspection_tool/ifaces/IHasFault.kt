package ru.webrelab.layout_inspection_tool.ifaces

import ru.webrelab.layout_inspection_tool.screen_difference.Difference
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max

interface IHasFault : IDecartCoordinates {
    fun maxPixelFault(other: IHasFault): Int {
        return max(
            abs(x() - other.x()),
            abs(y() - other.y())
        )
    }

    fun maxPercentFault(actual: IHasFault): Int {
        val leftFault = abs(x() - actual.x())
        val leftFaultPercentage = if (x() == 0) 100 else ceil(leftFault * 100f / x()).toInt()
        val topFault = abs(y() - actual.y())
        val topFaultPercentage = if (y() == 0) 100 else ceil(topFault * 100f / y()).toInt()
        return leftFaultPercentage.coerceAtLeast(topFaultPercentage)
    }

    fun fault(actual: IHasFault): Int {
        return maxPixelFault(actual).coerceAtMost(maxPercentFault(actual))
    }

    fun getDifferences(actual: IHasFault): List<Difference>

    fun getDifference(field: String, expected: Int, actual: Int): Difference? {
        return if (expected != actual)
            Difference(field, expected, actual)
        else null
    }
}
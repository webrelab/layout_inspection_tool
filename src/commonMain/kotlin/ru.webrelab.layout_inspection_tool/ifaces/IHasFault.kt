package ru.webrelab.layout_inspection_tool.ifaces

import kotlin.math.abs
import kotlin.math.max

interface IHasFault : IDecartCoordinates{
    fun maxPixelFault(other: IHasFault): Int {
        return max(
            abs(x() - other.x()),
            abs(y() - other.y())
        )
    }

    fun maxPercentFault(other: IHasFault): Int {
        val leftFault = abs(x() - other.x())
        val leftFaultPercentage = if (x() == 0) 100 else leftFault * 100 / x()
        val topFault = abs(y() - other.y())
        val topFaultPercentage = if (y() == 0) 100 else topFault * 100 / y()
        return leftFaultPercentage.coerceAtMost(topFaultPercentage)
    }

    fun fault(other: IHasFault): Int {
        return maxPixelFault(other).coerceAtMost(maxPercentFault(other))
    }
}
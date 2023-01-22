package ru.webrelab.layout_inspection_tool.repositories

import ru.webrelab.layout_inspection_tool.Executor
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType


class TestingPiece<E>(
    val name: String,
    val element: E,
    rowTypes: List<IMeasuringType>
) {
    val types: List<IMeasuringType>
    init {
        types = rowTypes.map { getTypes(it) }.flatten()
    }

    private fun getTypes(type: IMeasuringType): List<IMeasuringType> {
        if (type.isComplex) {
            return LitConfig.config<E>().measuringTypes
                .filter { !it.isComplex && !it.isPositionOnly }
                .toList()
        }
        return listOf(type)
    }
}
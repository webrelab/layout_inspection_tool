package ru.webrelab.layout_inspection_tool.screen_difference

import kotlin.native.concurrent.ThreadLocal

class Difference(
    private val name: String,
    private val expected: Any,
    private val actual: Any
) {
    @ThreadLocal
    companion object {
        var formatter: (Difference) -> String = {
            "Parameter ${it.name} has value ${it.actual} but expected ${it.expected}"
        }
    }

    override fun toString(): String {
        return formatter(this)
    }
}
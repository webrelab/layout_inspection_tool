package ru.webrelab.layout_inspection_tool.screen_difference

fun equalsWithMask(expected: Any, actual: Any) : Boolean {
    if (actual is String && expected is String) {
        if (!expected.contains("*")) return actual == expected
        if (expected == "*") return true
        if (Regex("^\\*+$").matches(expected)) return actual.length > 0
        val mustStartsWith: Boolean = !expected.startsWith("*")
        val mustEndsWith: Boolean = !expected.endsWith("*")
        val expectedParts: Array<String> = expected.split("\\*".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        var currentPosition = 0
        val last = expectedParts.size - 1
        for (i in expectedParts.indices) {
            val foundIndex: Int = actual.indexOf(expectedParts[i], currentPosition)
            if (foundIndex < 0) return false
            currentPosition = foundIndex + expectedParts[i].length
            if (i == 0 && mustStartsWith && foundIndex != 0) return false
            if (i == last && mustEndsWith && foundIndex != actual.length - expectedParts[last].length) return false
        }
        return true
    }
    return actual == expected
}
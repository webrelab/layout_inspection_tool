package ru.webrelab.layout_inspection_tool

actual fun dataToMap(data: Any): Map<String, Any> =
    (js("Object.entries") as (dynamic) -> Array<Array<Any>>)
    .invoke(data)
    .associate { entry -> entry[0] as String to entry[1] }
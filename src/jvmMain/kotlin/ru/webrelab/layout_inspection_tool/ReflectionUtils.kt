package ru.webrelab.layout_inspection_tool

import kotlin.reflect.full.memberProperties

actual fun dataToMap(data: Any): Map<String, Any> =
    data::class
        .memberProperties
        .associateTo(mutableMapOf()) { it.name to it.call(data) as Any }
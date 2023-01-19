package ru.webrelab.layout_inspection_tool.repositories

data class AdditionalWebData(
    val left: Int,
    val top: Int,
    val width: Int,
    val height: Int,
    val transform: String
)

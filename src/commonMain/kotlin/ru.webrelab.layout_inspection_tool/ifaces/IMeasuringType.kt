package ru.webrelab.layout_inspection_tool.ifaces

interface IMeasuringType {
    val id : String
    val color: String
    val deg: Int
    val isComplex: Boolean
    val isPositionOnly: Boolean
    fun <E> create(element: E): IRepository
}
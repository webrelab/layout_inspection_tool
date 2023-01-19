package ru.webrelab.layout_inspection_tool.ifaces

interface IWebRepository: IRepository {
    fun getTransform(): String
}
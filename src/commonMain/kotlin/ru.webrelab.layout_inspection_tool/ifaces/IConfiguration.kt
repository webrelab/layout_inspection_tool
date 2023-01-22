package ru.webrelab.layout_inspection_tool.ifaces

interface IConfiguration<E> {
    val measuringTypes: List<IMeasuringType>
    val screenSizes: List<IScreenSize>
    val behavior: ILayoutInspectionBehavior<E>
    val violation: Int
    val pathToDataset: String
}
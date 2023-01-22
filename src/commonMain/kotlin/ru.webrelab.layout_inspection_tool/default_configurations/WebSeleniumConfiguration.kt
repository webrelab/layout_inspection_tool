package ru.webrelab.layout_inspection_tool.default_configurations

import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.WebLayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.enums.BrowserScreenSize
import ru.webrelab.layout_inspection_tool.enums.WebMeasuringType
import ru.webrelab.layout_inspection_tool.ifaces.IConfiguration
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType
import ru.webrelab.layout_inspection_tool.ifaces.IScreenSize

class WebSeleniumConfiguration<E>(
    override val pathToDataset: String,
    override val behavior: WebLayoutInspectionBehavior<E>
) : IConfiguration<E> {
    override val violation = behavior.getViolation()
    override val measuringTypes: List<IMeasuringType> = listOf(*WebMeasuringType.values())
    override val screenSizes: List<IScreenSize> = listOf(*BrowserScreenSize.values())
}

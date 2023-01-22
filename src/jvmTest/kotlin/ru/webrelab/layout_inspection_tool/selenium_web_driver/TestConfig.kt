package ru.webrelab.layout_inspection_tool.selenium_web_driver

import org.openqa.selenium.WebElement
import ru.webrelab.layout_inspection_tool.enums.WebMeasuringType
import ru.webrelab.layout_inspection_tool.ifaces.IConfiguration
import ru.webrelab.layout_inspection_tool.ifaces.ILayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType
import ru.webrelab.layout_inspection_tool.ifaces.IScreenSize

class TestConfig : IConfiguration<WebElement> {
    override val measuringTypes: List<IMeasuringType> = listOf(*WebMeasuringType.values())
    override val screenSizes: List<IScreenSize>
        get() = TODO("Not yet implemented")
    override val behavior: ILayoutInspectionBehavior<WebElement>
        get() = TODO("Not yet implemented")
    override val violation: Int
        get() = TODO("Not yet implemented")
    override val pathToDataset: String
        get() = TODO("Not yet implemented")
}
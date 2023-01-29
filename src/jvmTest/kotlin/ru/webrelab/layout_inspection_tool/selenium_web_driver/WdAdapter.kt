package ru.webrelab.layout_inspection_tool.selenium_web_driver

import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.ifaces.IAdapter
import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.Size

class WdAdapter: IAdapter<WebElement> {
    override fun findElements(request: String): Collection<WebElement> {
        return WdEnv.driver.findElements(By.xpath(request))
    }

    override fun executeJsScript(js: String, vararg element: Any): Any? {
        return (WdEnv.driver as RemoteWebDriver).executeScript(js, *element)
    }

    override fun getWindowSize(): Size {
        val dimension = WdEnv.driver.manage().window().size
        return Size(dimension.width, dimension.height)
    }

    override fun setWindowSize(size: Size) {
        WdEnv.driver.manage().window().size = Dimension(size.width, size.height)
    }

    override fun getAttribute(attributeName: String, element: WebElement): String {
        return element.getAttribute(attributeName)
    }

    override fun getPosition(element: WebElement): Position {
        val styles = (LitConfig.config<WebElement>().behavior).getStyles(element)
        return Position((styles["absoluteLeft"] as Number).toInt(), (styles["absoluteTop"] as Number).toInt())
    }

    override fun findElements(element: WebElement, request: String): Collection<WebElement> {
        return element.findElements(By.xpath(request))
    }
}
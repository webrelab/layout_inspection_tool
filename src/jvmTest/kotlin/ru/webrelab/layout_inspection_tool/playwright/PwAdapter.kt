package ru.webrelab.layout_inspection_tool.playwright

import com.microsoft.playwright.ElementHandle
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.ifaces.IAdapter
import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.Size

class PwAdapter: IAdapter<ElementHandle> {
    override fun findElements(request: String): Collection<ElementHandle> {
        return PwEnv.page.querySelectorAll("xpath=$request")
    }

    override fun executeJsScript(js: String, vararg element: Any): Any? {
        if (element.size > 1) {
            return PwEnv.page.evaluate(js, listOf(*element))
        } else if (element.size == 1) {
            return PwEnv.page.evaluate(js, element[0])
        }
        return PwEnv.page.evaluate(js)
    }

    override fun getWindowSize(): Size {
        val size = PwEnv.page.viewportSize()
        return Size(size.width, size.height)
    }

    override fun setWindowSize(size: Size) {
        PwEnv.page.setViewportSize(size.width, size.height)
    }

    override fun getAttribute(attributeName: String, element: ElementHandle): String {
        return element.getAttribute(attributeName)
    }

    override fun getPosition(element: ElementHandle): Position {
        val styles = (LitConfig.config<ElementHandle>().behavior).getStyles(element)
        return Position((styles["absoluteLeft"] as Number).toInt(), (styles["absoluteTop"] as Number).toInt())
    }

    override fun findElements(element: ElementHandle, request: String): Collection<ElementHandle> {
        return element.querySelectorAll("xpath=$request")
    }
}
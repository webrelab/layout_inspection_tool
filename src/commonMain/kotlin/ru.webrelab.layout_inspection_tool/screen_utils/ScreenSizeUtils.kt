package ru.webrelab.layout_inspection_tool.screen_utils

import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.LitException
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.WebLayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.getViewportSize
import ru.webrelab.layout_inspection_tool.ifaces.IScreenSize
import ru.webrelab.layout_inspection_tool.repositories.Size
import kotlin.math.abs

fun setBrowserViewportSize(screenSize: IScreenSize) {
    val behavior: WebLayoutInspectionBehavior<Any> = LitConfig.config<Any>().behavior as WebLayoutInspectionBehavior<Any>
    val adapter = behavior.adapter
    var viewPort = getViewportSize { behavior.executeJs(it) }
    var counter = 0
    while (
        abs(viewPort.height - screenSize.height) > 2 ||
        abs(viewPort.width - screenSize.width) > 2
    ) {
        val windowSize = adapter.getWindowSize()
        val newWindowSize = Size(
            windowSize.width - viewPort.width + screenSize.width,
            windowSize.height - viewPort.height + screenSize.height
        )
        adapter.setWindowSize(newWindowSize)
        viewPort = getViewportSize { (LitConfig.config<Any>().behavior as WebLayoutInspectionBehavior).executeJs(it) }
        if (++counter > 5) throw LitException("Screen resolution $screenSize can't be set")
    }
}

fun determineScreenSize(): IScreenSize {
    val behavior: WebLayoutInspectionBehavior<Any> = LitConfig.config<Any>().behavior as WebLayoutInspectionBehavior<Any>
    val config = LitConfig.config<Any>()
    val viewPort = getViewportSize { behavior.executeJs(it) }
    return config.screenSizes.find { abs(viewPort.height - it.height) < 2 && abs(viewPort.width - it.width) < 2 }
        ?: throw LitException("The current screen size (w: ${viewPort.width}, h: ${viewPort.height}) doesn't match the standard sizes")
}
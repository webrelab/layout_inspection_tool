package ru.webrelab.layout_inspection_tool.screen_utils

import ru.webrelab.layout_inspection_tool.LayoutElement
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.WebLayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.greedDraw
import ru.webrelab.layout_inspection_tool.ifaces.IDrawer
import ru.webrelab.layout_inspection_tool.ifaces.IElement
import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault

class WebBoxDrawer<E> : IDrawer<E> {
    private var canvasIsDraw: Boolean = false
    override fun isDrawPossible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun drawCanvas() {
        if (!canvasIsDraw) {
            ru.webrelab.layout_inspection_tool.drawCanvas { (LitConfig.config<Any>().behavior as WebLayoutInspectionBehavior).executeJs(it) }
            canvasIsDraw = true
        }
    }

    override fun drawComparisonFault(comparisonFaultList: Collection<ComparisonFault<E>>) {
        drawCanvas()
        comparisonFaultList.forEach {
            if (it.isActualAbsent() || it.hasDiff()) {
                drawElement(it.getExpected() as IElement<E>, "EXPECTED")
            }
            if (it.isExpectedAbsent() || it.hasDiff()) {
                drawElement(it.getActual() as IElement<E>, "ACTUAL")
            }
        }
    }

    override fun drawElement(element: IElement<E>, vararg color: String) {
        drawCanvas()
        val state = if (color.isEmpty()) null else color[0]
        greedDraw(
            {s, e -> (LitConfig.config<Any>().behavior as WebLayoutInspectionBehavior).executeJs(s, e)},
            element as LayoutElement<E>,
            state
        )
    }
}
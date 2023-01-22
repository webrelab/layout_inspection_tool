package ru.webrelab.layout_inspection_tool.default_configurations.behaviors

import ru.webrelab.layout_inspection_tool.ifaces.IAdapter
import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault

class SeleniumWebLayoutInspectionBehavior<E>(
    violation: Int,
    adapter: IAdapter<E>,
    actionBeforeTesting: () -> Unit,
    reportFailures: (List<ComparisonFault<E>>) -> Unit
) : WebLayoutInspectionBehavior<E>(violation, adapter, actionBeforeTesting, reportFailures) {

    override fun executeJs(js: String, vararg element: Any): Any? {
        return adapter.executeJsScript("return (function (e) {return handler(e);}(arguments[0]));\n${js}", *element)
    }
}
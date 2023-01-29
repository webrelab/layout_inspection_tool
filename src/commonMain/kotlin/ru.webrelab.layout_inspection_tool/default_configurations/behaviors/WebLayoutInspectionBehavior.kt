package ru.webrelab.layout_inspection_tool.default_configurations.behaviors

import ru.webrelab.layout_inspection_tool.*
import ru.webrelab.layout_inspection_tool.ifaces.IAdapter
import ru.webrelab.layout_inspection_tool.ifaces.IDrawer
import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault
import ru.webrelab.layout_inspection_tool.screen_difference.DefaultSifter
import ru.webrelab.layout_inspection_tool.screen_difference.DefaultWebScanner
import ru.webrelab.layout_inspection_tool.screen_utils.WebBoxDrawer

abstract class WebLayoutInspectionBehavior<E>(
    private val violation: Int,
    adapter: IAdapter<E>,
    actionBeforeTesting: () -> Unit,
    reportFailures: (List<ComparisonFault<E>>) -> Unit
) : AbstractLayoutInspectionBehavior<E>(
    DefaultSifter(violation),
    DefaultWebScanner(),
    adapter,
    actionBeforeTesting,
    reportFailures
) {

    fun getViolation() = violation

    override val drawer: IDrawer<E> = WebBoxDrawer()

    override fun screenPreparation() {
        measureText { executeJs(it) }
        measurePseudoElements { executeJs(it) }
        measureDecor { executeJs(it) }
    }


    open fun executeJs(js: String, vararg element: Any): Any? {
        throw LitException("Method doesn't implement")
    }

    override fun getStyles(element: E): Map<String, Any> {
        @Suppress("UNCHECKED_CAST")
        val data= getStyles({ s,e -> executeJs(s, e)}, element as Any)
        return data.filter { it.size == 2 }
            .map { it["name"] as String to it["value"] }
            .filterNot { it.second == null }
            .associateTo(mutableMapOf()) { it.first to it.second as Any }
    }
}
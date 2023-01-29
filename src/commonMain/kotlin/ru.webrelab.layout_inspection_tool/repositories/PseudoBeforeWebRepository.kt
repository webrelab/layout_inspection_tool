package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.WebLayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.pseudoScan

@Serializable
@SerialName("PseudoBeforeWebRepository")
data class PseudoBeforeWebRepository(
    val content: String,
    val color: String,
    val background: String
): AbstractWebRepository() {
    companion object {
        fun <E> init(element: E): PseudoBeforeWebRepository {
            val behavior = LitConfig.config<E>().behavior as WebLayoutInspectionBehavior
            val data = pseudoScan({ s, e -> behavior.executeJs(s, e)}, element as Any)
            val repository = PseudoBeforeWebRepository(
                content = data["before"]!!["content"] as String,
                color =  data["before"]!!["color"] as String,
                background = data["before"]!!["background"] as String
            )
            repository.setAdditionalData(repository.getAdditionalData(data["before"]!!))
            return repository
        }
    }
}
package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.WebLayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.pseudoScan

@Serializable
@SerialName("PseudoAfterWebRepository")
data class PseudoAfterWebRepository(
    val content: String,
    val color: String,
    val background: String
): AbstractWebRepository() {
    companion object {
        fun <E> init(element: E): PseudoAfterWebRepository {
            val behavior = LitConfig.config<E>().behavior as WebLayoutInspectionBehavior
            val data = pseudoScan({ s, e -> behavior.executeJs(s, e)}, element as Any)
            val repository = PseudoAfterWebRepository(
                content = data["after"]!!["content"] as String,
                color =  data["after"]!!["color"] as String,
                background = data["after"]!!["background"] as String
            )
            repository.setAdditionalData(repository.getAdditionalData(data["after"]!!))
            return repository
        }
    }
}
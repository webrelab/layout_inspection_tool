package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.WebLayoutInspectionBehavior

@Serializable
@SerialName("ImageWebRepository")
data class ImageWebRepository(
    val src: String
): AbstractWebRepository() {
    companion object {
        fun <E> init(element: E): ImageWebRepository {
            val behavior: WebLayoutInspectionBehavior<E> =
                LitConfig.config<E>().behavior as WebLayoutInspectionBehavior<E>
            val src = behavior.adapter.getAttribute("src", element)
            val styles = behavior.getStyles(element)
            val repository = ImageWebRepository(
                clearUrl(src)
            )
            repository.setAdditionalData(repository.getAdditionalData(styles))
            return repository
        }

        private fun clearUrl(url: String): String {
            if (url.startsWith("http")) {
                val i = url.indexOf('/', 9)
                return url.substring(i)
            }
            return url
        }
    }
}
package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.ifaces.IConfiguration

@Serializable
@SerialName("DecorWebRepository")
data class DecorWebRepository(
    val border: String,
    val borderRadius: String,
    val boxShadow: String,
    val background: String,
    val opacity: String
): AbstractWebRepository() {
    companion object {
        fun <E> init(element: E): DecorWebRepository {
            val config: IConfiguration<E> = LitConfig.config()
            val styles = config.behavior.getStyles(element)
            val repository = DecorWebRepository(
                border = styles["border"] as String,
                borderRadius = styles["borderRadius"] as String,
                boxShadow = styles["boxShadow"] as String,
                background = styles["background"] as String,
                opacity = styles["opacity"] as String
            )
            repository.setAdditionalData(repository.getAdditionalData(styles))
            return repository
        }
    }
}
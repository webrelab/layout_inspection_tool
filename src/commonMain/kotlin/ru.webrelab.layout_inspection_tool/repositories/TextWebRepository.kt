package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.ifaces.IConfiguration

@Serializable
@SerialName("TextWebRepository")
data class TextWebRepository(
    val fontFamily: String,
    val fontSize: String,
    val fontWeight: String,
    val color: String,
    val fontStyle: String,
    val fontVariant: String,
    val textDecoration: String,
    val textShadow: String,
    val innerText: String
) : AbstractWebRepository() {
    companion object {
        fun <E> init(element: E): TextWebRepository {
            val config: IConfiguration<E> = LitConfig.config()
            val styles = config.behavior.getStyles(element)
            val repository = TextWebRepository(
                styles["fontFamily"] as String,
                styles["fontSize"] as String,
                styles["fontWeight"] as String,
                styles["color"] as String,
                styles["fontStyle"] as String,
                styles["fontVariant"] as String,
                styles["textDecoration"] as String,
                styles["textShadow"] as String,
                styles["innerText"] as String
            )
            repository.setAdditionalData(repository.getAdditionalData(styles))
            return repository
        }
    }
}
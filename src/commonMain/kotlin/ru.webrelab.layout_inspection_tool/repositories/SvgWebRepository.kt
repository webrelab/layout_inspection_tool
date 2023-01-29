package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.LitException
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.WebLayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.ifaces.IRepository
import ru.webrelab.layout_inspection_tool.screen_difference.Difference
import ru.webrelab.layout_inspection_tool.screen_difference.equalsWithMask
import ru.webrelab.layout_inspection_tool.svgScan

@Serializable
@SerialName("SvgWebRepository")
data class SvgWebRepository(
    val fill: String,
    val vectors: List<Map<String, String>>
) : AbstractWebRepository() {
    companion object {
        fun <E> init(element: E): SvgWebRepository {
            val behavior = LitConfig.config<E>().behavior as WebLayoutInspectionBehavior<E>
            val response = svgScan({ s,e -> behavior.executeJs(s, e) }, element as Any)
            val styles = behavior.getStyles(element)

            @Suppress("UNCHECKED_CAST")
            val vectors: List<Map<String, String>> = response
                .map {
                    it.filter { e -> e.value != null }
                        .mapValues { e -> e.value!!.toString() }
                }
            val repository = SvgWebRepository(
                fill = styles["fill"] as String,
                vectors = vectors
            )
            repository.setAdditionalData(repository.getAdditionalData(styles))
            return repository
        }
    }

    override fun compare(actual: IRepository): List<Difference> {
        if (actual !is SvgWebRepository) throw LitException("Expected class 'SvgWebRepository' doesn't equals actual class '${actual::class.simpleName}'")
        val differences = mutableListOf<Difference>()
        if (!equalsWithMask(fill, actual.fill)) differences.add(Difference("fill", fill, actual.fill))
        val actualIterator = actual.vectors.iterator()
        val expectedIterator = vectors.iterator()
        val jsonFormat = Json { prettyPrint = true }
        while (actualIterator.hasNext() && expectedIterator.hasNext()) {
            val actualMap = actualIterator.next()
            val expectedMap = expectedIterator.next()
            if (actualMap != expectedMap) {
                differences.add(
                    Difference(
                        "svg attributes",
                        "\n${jsonFormat.encodeToString(expectedMap)}",
                        "\n${jsonFormat.encodeToString(actualMap)}"
                    )
                )
            }
        }
        while (actualIterator.hasNext()) {
            differences.add(
                Difference("extra svg elements", "", "\n${jsonFormat.encodeToString(actualIterator.next())}")
            )
        }
        while (expectedIterator.hasNext()) {
            differences.add(
                Difference("lost svg elements", "\n${jsonFormat.encodeToString(expectedIterator.next())}", "")
            )
        }
        return differences
    }
}
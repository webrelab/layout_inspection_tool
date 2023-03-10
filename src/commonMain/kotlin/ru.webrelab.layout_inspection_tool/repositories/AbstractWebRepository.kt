package ru.webrelab.layout_inspection_tool.repositories

import kotlinx.serialization.Transient
import ru.webrelab.layout_inspection_tool.dataToMap
import ru.webrelab.layout_inspection_tool.ifaces.IRepository
import ru.webrelab.layout_inspection_tool.ifaces.IWebRepository
import ru.webrelab.layout_inspection_tool.screen_difference.Difference
import ru.webrelab.layout_inspection_tool.screen_difference.equalsWithMask

abstract class AbstractWebRepository : IWebRepository {
    @Transient
    private var additionalWebData: AdditionalWebData? = null
    override fun getSize() = Size(additionalWebData?.width ?: 0, additionalWebData?.height ?: 0)
    override fun getPosition() = Position(additionalWebData?.left ?: 0, additionalWebData?.top ?: 0)
    override fun getRelativePosition(container: Position) = getPosition().getRelativePosition(container)

    override fun getTransform() = additionalWebData?.transform ?: ""
    override fun dataMap(): Map<String, Any> = dataToMap(this)
    fun setAdditionalData(additionalWebData: AdditionalWebData) {
        this.additionalWebData = additionalWebData
    }

    override fun compare(actual: IRepository): List<Difference> {
        val expectedData = dataMap()
        val actualData = actual.dataMap()
        val differences = mutableListOf<Difference>()
        for (field in expectedData.keys) {
            if (equalsWithMask(expectedData[field]!!, actualData[field]!!)) continue
            differences.add(
                Difference(field, expectedData[field]!!, actualData[field]!!)
            )
        }
        return differences
    }

    protected fun getAdditionalData(styles: Map<String, Any>) = AdditionalWebData(
        (styles["absoluteLeft"] as Number).toInt(),
        (styles["absoluteTop"] as Number).toInt(),
        (styles["absoluteWidth"] as Number).toInt(),
        (styles["absoluteHeight"] as Number).toInt(),
        styles["transform"] as String
    )
}
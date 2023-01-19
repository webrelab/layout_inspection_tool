package ru.webrelab.layout_inspection_tool.enums

import ru.webrelab.layout_inspection_tool.ifaces.IRepository
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType
import ru.webrelab.layout_inspection_tool.repositories.TextWebRepository
import ru.webrelab.layout_inspection_tool.screen_difference.Difference

enum class WebMeasuringType(
    override val id: String,
    override val color: String,
    override val deg: Int,
    override val isComplex: Boolean,
    override val isPositionOnly: Boolean,
    private val createFunction: (element: Any) -> IRepository
) : IMeasuringType {
    POSITION(".", "#000", 45, false, true, { ALL.fakeObject() }),
    ALL("", "RED", 15, true, false, { ALL.fakeObject() }),
    TEXT(
        "descendant-or-self::*[contains(@class, 'measuringTypeText')]",
        "#1f618d",
        30,
        false,
        false,
        { TextWebRepository.init(it) }),
    ;
    override fun <E> create(element: E): IRepository = createFunction.invoke(element as Any)

    private fun fakeObject(): IRepository = object : IRepository {
        override fun dataMap(): Map<String, Any> {
            TODO("Not yet implemented")
        }

        override fun compare(other: IRepository): List<Difference> {
            TODO("Not yet implemented")
        }
    }
}
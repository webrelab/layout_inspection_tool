package ru.webrelab.layout_inspection_tool.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.webrelab.layout_inspection_tool.ifaces.IRepository
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType
import ru.webrelab.layout_inspection_tool.repositories.*
import ru.webrelab.layout_inspection_tool.screen_difference.Difference

@Serializable
@SerialName("webMeasuringType")
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
        { TextWebRepository.init(it) }
    ),
    DECOR(
        "descendant-or-self::*[contains(@class, 'measuringTypeDecor')]",
        "#6c3483",
        60,
        false,
        false,
        { DecorWebRepository.init(it) }
    ),
    SVG(
        "descendant-or-self::*[name() = 'svg']",
        "#cd6155",
        -90,
        false,
        false,
        { SvgWebRepository.init(it) }
    ),
    PSEUDO_BEFORE(
        "descendant-or-self::*[contains(@class, 'measuringBeforeElement')]",
        "#b9770e",
        -30,
        false,
        false,
        { PseudoBeforeWebRepository.init(it) }
    ),
    PSEUDO_AFTER(
        "descendant-or-self::*[contains(@class, 'measuringAfterElement')]",
        "#770eb9",
        -60,
        false,
        false,
        { PseudoAfterWebRepository.init(it) }
    ),
    IMAGE(
        "descendant-or-self::img",
        "#148f77",
        0,
        false,
        false,
        { ImageWebRepository.init(it) }
    )
    ;

    override fun <E> create(element: E): IRepository = createFunction.invoke(element as Any)

    private fun fakeObject(): IRepository = object : IRepository {
        override fun dataMap(): Map<String, Any> {
            TODO("Not yet implemented")
        }

        override fun compare(actual: IRepository): List<Difference> {
            TODO("Not yet implemented")
        }

        override fun getSize(): Size {
            TODO("Not yet implemented")
        }

        override fun getPosition(): Position {
            TODO("Not yet implemented")
        }

        override fun getRelativePosition(container: Position): Position {
            TODO("Not yet implemented")
        }
    }
}
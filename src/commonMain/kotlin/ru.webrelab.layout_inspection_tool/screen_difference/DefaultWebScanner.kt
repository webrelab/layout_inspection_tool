package ru.webrelab.layout_inspection_tool.screen_difference

import ru.webrelab.layout_inspection_tool.LayoutElement
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.LitException
import ru.webrelab.layout_inspection_tool.ifaces.IConfiguration
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType
import ru.webrelab.layout_inspection_tool.ifaces.IScanner
import ru.webrelab.layout_inspection_tool.ifaces.IWebRepository
import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.TestingPiece

class DefaultWebScanner<E> : IScanner<E> {
    override fun scan(testingPiece: List<TestingPiece<E>>, container: Position?): List<LayoutElement> {
        return testingPiece
            .map { scan(it, container) }
            .flatten()
            .toList()
    }

    private fun scan(piece: TestingPiece<E>, container: Position?): List<LayoutElement> {
        val layoutElements = mutableListOf<LayoutElement>()

        val config: IConfiguration<E> = LitConfig.config()
        piece.types.forEach {
            if (it.isPositionOnly) {
                if (container == null) throw LitException("You must set the container for element with measuring type POSITION")
                val element = create(piece.element, it, container, piece.name)
                if (element != null) layoutElements.add(element)
            } else {
                val ct = container ?: it.create(piece.element).getPosition()
                val elements = mutableListOf<E>()
                elements.addAll(config.behavior.adapter.findElements(piece.element, it.id))
                elements.forEach { e ->
                    val element = create(e, it, ct, piece.name)
                    if (element != null) layoutElements.add(element)
                }
            }
        }
        return layoutElements
    }

    private fun create(element: E, type: IMeasuringType, container: Position, name: String): LayoutElement? {
        val repository = type.create(element)
        val size = repository.getSize()
        if (size.isEmpty()) return null
        val position = repository.getRelativePosition(container)
        val transform = (repository as IWebRepository).getTransform()
        return LayoutElement(
            name,
            false,
            type,
            position,
            size,
            container,
            repository,
            transform
        )
    }
}
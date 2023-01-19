package ru.webrelab.layout_inspection_tool.ifaces

import ru.webrelab.layout_inspection_tool.LayoutElement
import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.TestingPiece

interface IScanner<E> {
    fun scan(testingPiece: List<TestingPiece<E>>, container: Position?): List<LayoutElement<E>>
}
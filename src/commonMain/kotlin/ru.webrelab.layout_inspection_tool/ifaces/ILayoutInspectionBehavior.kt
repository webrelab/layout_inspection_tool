package ru.webrelab.layout_inspection_tool.ifaces

import okio.Path
import ru.webrelab.layout_inspection_tool.LayoutElement
import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.TestingPiece
import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault

interface ILayoutInspectionBehavior<E> {
    val drawer: IDrawer<E>
    val sifter: ISifter<E>
    val scanner: IScanner<E>
    val adapter: IAdapter<E>
    val actionBeforeTesting: () -> Unit
    val reportFailures: (List<ComparisonFault<E>>) -> Unit
    fun screenPreparation()
    fun scanScreen(testingPieces: List<TestingPiece<E>>, container: Position?)
    fun getElements(): Map<String, IElement>
    fun isDataPresent(path: Path): Boolean
    fun loadData(path: Path): String
    fun saveData(path: Path)
    fun deserialize(storedData: String): Map<String, LayoutElement>
    fun drawGreed(elements: Collection<IElement>, vararg color: String) = drawer.drawElements(elements, *color)
    fun serialize(data: Map<String, LayoutElement>): String
    fun getStyles(element: E): Map<String, Any>
}
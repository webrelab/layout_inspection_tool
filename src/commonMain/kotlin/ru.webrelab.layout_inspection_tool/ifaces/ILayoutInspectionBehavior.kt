package ru.webrelab.layout_inspection_tool.ifaces

import okio.Path
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
    fun getElements(): Map<String, IElement<E>>
    fun isDataPresent(path: Path): Boolean
    fun loadData(path: Path): String
    fun saveData(path: Path)
    fun deserialize(storedData: String): Map<String, IElement<E>>
    fun drawGreed(elements: Collection<IElement<E>>, vararg color: String) = drawer.drawElements(elements, *color)
    fun serialize(data: Map<String, IElement<E>>): String
    fun getStyles(element: E): Map<String, Any>
}
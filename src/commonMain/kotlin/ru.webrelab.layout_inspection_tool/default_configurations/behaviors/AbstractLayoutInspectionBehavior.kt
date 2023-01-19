package ru.webrelab.layout_inspection_tool.default_configurations.behaviors

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.Path
import ru.webrelab.layout_inspection_tool.enums.Environment
import ru.webrelab.layout_inspection_tool.ifaces.*
import ru.webrelab.layout_inspection_tool.isFileExist
import ru.webrelab.layout_inspection_tool.readFile
import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.TestingPiece
import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault
import ru.webrelab.layout_inspection_tool.writeData

abstract class AbstractLayoutInspectionBehavior<E>(
    override val sifter: ISifter<E>,
    override val scanner: IScanner<E>,
    override val adapter: IAdapter<E>,
    override val actionBeforeTesting: () -> Unit,
    override val reportFailures: (List<ComparisonFault<E>>) -> Unit
): ILayoutInspectionBehavior<E> {
    private val scannedElements: MutableMap<String, IElement<E>> = mutableMapOf()
    override fun isDataPresent(path: Path): Boolean = isFileExist(path)
    override fun loadData(path: Path): String = readFile(path)
    override fun saveData(path: Path) = writeData(path, serialize(getElements()))
    fun getPath(scenarioName: String, environment: Environment, screenSize: IScreenSize,vararg pathToScenario: String): Path =
        ru.webrelab.layout_inspection_tool.getPath(scenarioName, "json", environment, screenSize, *pathToScenario)

    override fun scanScreen(testingPieces: List<TestingPiece<E>>, container: Position?) {
        scanner
            .scan(testingPieces, container)
            .forEach { scannedElements[it.id] = it }
    }

    override fun getElements(): Map<String, IElement<E>> {
        return scannedElements
    }

    override fun deserialize(storedData: String): Map<String, IElement<E>> {
        return Json.decodeFromString(storedData)
    }

    override fun serialize(data: Map<String, IElement<E>>): String {
        return Json.encodeToString(data)
    }
}
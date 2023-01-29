package ru.webrelab.layout_inspection_tool.default_configurations.behaviors

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import okio.Path
import ru.webrelab.layout_inspection_tool.*
import ru.webrelab.layout_inspection_tool.enums.Environment
import ru.webrelab.layout_inspection_tool.ifaces.*
import ru.webrelab.layout_inspection_tool.repositories.*
import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault

abstract class AbstractLayoutInspectionBehavior<E>(
    override val sifter: ISifter<E>,
    override val scanner: IScanner<E>,
    override val adapter: IAdapter<E>,
    override val actionBeforeTesting: () -> Unit,
    override val reportFailures: (List<ComparisonFault<E>>) -> Unit
) : ILayoutInspectionBehavior<E> {
    private val scannedElements: MutableMap<String, LayoutElement> = mutableMapOf()
    override fun isDataPresent(path: Path): Boolean = isFileExist(path)
    override fun loadData(path: Path): String = readFile(path)
    override fun saveData(path: Path) = writeData(path, serialize(getElements()))
    private val format = Json {
        serializersModule =
            SerializersModule {
                polymorphic(IRepository::class) {
                    subclass(TextWebRepository::class, TextWebRepository.serializer())
                    subclass(DecorWebRepository::class, DecorWebRepository.serializer())
                    subclass(SvgWebRepository::class, SvgWebRepository.serializer())
                    subclass(PseudoBeforeWebRepository::class, PseudoBeforeWebRepository.serializer())
                    subclass(PseudoAfterWebRepository::class, PseudoAfterWebRepository.serializer())
                    subclass(ImageWebRepository::class, ImageWebRepository.serializer())
                }
            }
    }

    fun getPath(
        scenarioName: String,
        environment: Environment,
        screenSize: IScreenSize,
        vararg pathToScenario: String
    ): Path =
        getPath(scenarioName, "json", environment, screenSize, *pathToScenario)

    override fun scanScreen(testingPieces: List<TestingPiece<E>>, container: Position?) {
        scanner
            .scan(testingPieces, container)
            .forEach { scannedElements[it.id] = it }
    }

    override fun getElements(): Map<String, LayoutElement> {
        return scannedElements
    }

    override fun deserialize(storedData: String): Map<String, LayoutElement> {
        return format.decodeFromString(storedData)
    }

    override fun serialize(data: Map<String, LayoutElement>): String {
        return format.encodeToString(data)
    }
}
package ru.webrelab.layout_inspection_tool

import okio.Path
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.AbstractLayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.enums.Environment
import ru.webrelab.layout_inspection_tool.ifaces.*
import ru.webrelab.layout_inspection_tool.repositories.Position
import ru.webrelab.layout_inspection_tool.repositories.TestingPiece
import ru.webrelab.layout_inspection_tool.screen_utils.determineScreenSize

class Executor<E>(
    private val testingPieces: List<TestingPiece<E>>,
    private val containerElement: E,
    private val scenarioName: String,
    private val environment: Environment,
    private vararg val pathToScenario: String,
) {
    fun execute() {
        val screenSize = determineScreenSize()
        val behavior: ILayoutInspectionBehavior<E> = LitConfig.config<E>().behavior
        val adapter: IAdapter<E> = behavior.adapter
        behavior.actionBeforeTesting.invoke()
        behavior.screenPreparation()
        val container: Position? = when (containerElement) {
            null -> null
            else -> adapter.getPosition(containerElement)
        }
        behavior.scanScreen(testingPieces, container)
        val path = (behavior as AbstractLayoutInspectionBehavior).getPath(
            scenarioName, environment, screenSize, *pathToScenario
        )
        if (behavior.isDataPresent(path)) {
            compare(behavior, path)
        } else {
            behavior.saveData(path)
            behavior.drawGreed(behavior.getElements().values)
        }
    }

    private fun compare(behavior: ILayoutInspectionBehavior<E>, path: Path) {
        val expected = behavior.deserialize(behavior.loadData(path))
        copyContainers(expected, behavior.getElements())
        behavior.sifter.combine(
            expected,
            behavior.getElements()
        )
        behavior.sifter.sift()
        val faults = behavior.sifter.getFaultList()
        if (faults.isNotEmpty()) {
            behavior.drawer.drawComparisonFault(faults)
            behavior.reportFailures.invoke(faults)
        }
    }

    private fun copyContainers(expected: Map<String, IElement<E>>, actual: Map<String, IElement<E>>) {
       val containers = actual.values
            .distinctBy { it.name }
            .associateTo(mutableMapOf()) {
                it.name to it.container
            }
        expected.forEach {
            val name = it.value.name
            if (containers.containsKey(name)) it.value.container = containers[name] as Position
        }
    }



}
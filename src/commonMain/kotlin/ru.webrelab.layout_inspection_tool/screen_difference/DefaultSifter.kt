package ru.webrelab.layout_inspection_tool.screen_difference

import ru.webrelab.layout_inspection_tool.ifaces.IElement
import ru.webrelab.layout_inspection_tool.ifaces.ISifter
import kotlin.math.max

class DefaultSifter<E>(
    override val violation: Int
) : ISifter<E> {
    private val expected = mutableMapOf<String, IElement>()
    private val actual = mutableMapOf<String, IElement>()
    private val pairs = mutableListOf<ElementPair<E>>()
    private val failList = mutableListOf<ComparisonFault<E>>()
    private val siftedExpected = mutableListOf<String>()
    private val siftedActual = mutableListOf<String>()
    private val goodPairs = mutableListOf<ElementPair<E>>()
    private val badPairs = mutableListOf<ElementPair<E>>()

    override fun combine(expected: Map<String, IElement>, actual: Map<String, IElement>) {
        this.expected.putAll(expected)
        this.actual.putAll(actual)
        val registeredExpected = mutableSetOf<String>()
        val registeredActual = mutableSetOf<String>()
        expected.forEach { e ->
            actual.forEach { a ->
                if (e.value.name == a.value.name && e.value.type == a.value.type) {
                    pairs.add(ElementPair(e.value, a.value))
                    registeredExpected.add(e.key)
                    registeredActual.add(a.key)
                }
            }
        }
        expected.keys.forEach {
            if (!registeredExpected.contains(it)) {
                failList.add(ComparisonFault(expected[it], null, null))
            }
        }
        actual.keys.forEach {
            if (!registeredActual.contains(it)) {
                failList.add(ComparisonFault(null, actual[it], null))
            }
        }
    }

    override fun sift() {
        siftEqualsWithViolation()
        siftDifferentData()
        siftDifferentSizeAndPosition()
        collectLeftovers()
    }

    private fun siftEqualsWithViolation() {
        sift(
            { listOf() },
            { it.equalWithViolation(violation) && it.isEqualId },
            compareBy { it.fault },
            { goodPairs.add(it) }
        )
        sift(
            { listOf() },
            { it.equalWithViolation(violation) },
            compareBy { it.fault },
            { goodPairs.add(it) }
        )
    }

    private fun siftDifferentData() {
        sift(
            { it.dataDifferenceList },
            { it.notEqualDataUnderViolation(violation) && it.isEqualId },
            compareBy { it.fault },
            { badPairs.add(it) }
        )
        sift(
            { it.dataDifferenceList },
            { it.notEqualDataUnderViolation(violation) },
            compareBy { it.fault },
            { badPairs.add(it) }
        )
    }

    private fun siftDifferentSizeAndPosition() {
        sift(
            { it.getMetricDifference(violation) },
            { it.isDataEqual && it.isEqualId },
            compareBy { it.fault },
            { badPairs.add(it) }
        )
        sift(
            { it.getMetricDifference(violation) },
            { it.isDataEqual },
            compareBy { it.fault },
            { badPairs.add(it) }
        )
    }

    private fun collectLeftovers() {
        expected.filter { !siftedExpected.contains(it.key) }
            .forEach { failList.add(ComparisonFault(it.value, null, null)) }
        actual.filter { !siftedActual.contains(it.key) }
            .forEach { failList.add(ComparisonFault(null, it.value, null)) }
    }

    private fun sift(
        diff: (ElementPair<E>) -> List<Difference>,
        filter: (ElementPair<E>) -> Boolean,
        sort: (Comparator<ElementPair<E>>),
        log: (ElementPair<E>) -> Unit
    ) {
        val firstExpected = mutableSetOf<String>()
        val firstActual = mutableSetOf<String>()
        pairs
            .filter {
                !siftedExpected.contains(it.expected.id) &&
                        !siftedActual.contains(it.actual.id)
            }
            .filter(filter)
            .sortedWith(sort)
            .forEach {
                if (!firstExpected.contains(it.expected.id) && !firstActual.contains(it.actual.id)) {
                    siftedExpected.add(it.expected.id)
                    siftedActual.add(it.actual.id)
                    firstExpected.add(it.expected.id)
                    firstActual.add(it.actual.id)
                    val diffList = diff.invoke(it)
                    if (diffList.isNotEmpty()) failList.add(ComparisonFault(it.expected, it.actual, diffList))
                    log.invoke(it)
                }
            }
        clear()
    }

    private fun clear() {
        pairs.removeAll { siftedExpected.contains(it.expected.id) && siftedActual.contains(it.actual.id) }
    }


    override fun getFaultList(): List<ComparisonFault<E>> = failList

    class ElementPair<E>(
        val expected: IElement,
        val actual: IElement
    ) {
        private val sizeFault = expected.size.fault(actual.size)
        private val positionFault = expected.position.fault(actual.position)
        val fault = max(sizeFault, positionFault)
        val dataDifferenceList = expected.data.compare(actual.data)
        val isDataEqual = dataDifferenceList.isEmpty()
        val isEqualId = expected.id == actual.id
        fun equalWithViolation(violation: Int) = isDataEqual && fault <= violation
        fun notEqualDataUnderViolation(violation: Int) =
            !isDataEqual && fault <= violation

        fun getMetricDifference(violation: Int): List<Difference> {
            val diff = mutableListOf<Difference>()
            if (sizeFault > violation) {
                diff.addAll(expected.size.getDifferences(actual.size))
            }
            if (positionFault > violation) {
                diff.addAll(expected.position.getDifferences(actual.position))
            }
            return diff
        }
    }

}
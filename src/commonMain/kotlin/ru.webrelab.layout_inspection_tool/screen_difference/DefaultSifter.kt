package ru.webrelab.layout_inspection_tool.screen_difference

import ru.webrelab.layout_inspection_tool.ifaces.IElement
import ru.webrelab.layout_inspection_tool.ifaces.ISifter
import kotlin.math.max

class DefaultSifter<E>(
    override val violation: Int
) : ISifter<E> {
    private val expected = mutableMapOf<String, IElement<E>>()
    private val actual = mutableMapOf<String, IElement<E>>()
    private val pairs = mutableListOf<ElementPair<E>>()
    private val failList = mutableListOf<ComparisonFault<E>>()
    private val siftedExpected = mutableListOf<String>()
    private val siftedActual = mutableListOf<String>()

    override fun combine(expected: Map<String, IElement<E>>, actual: Map<String, IElement<E>>) {
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
            { it.equalWithViolation(violation) },
            compareBy { it.fault }
        )
    }

    private fun siftDifferentData() {
        sift(
            { it.differenceList },
            { it.notEqualDataUnderViolation(violation) },
            compareBy { it.fault }
        )
    }

    private fun siftDifferentSizeAndPosition() {
        sift(
            { it.differenceList },
            { it.isDataEqual },
            compareBy { it.fault }
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
        sort: (Comparator<ElementPair<E>>)
    ) {
        val firstExpected = mutableSetOf<String>()
        val firstActual = mutableSetOf<String>()
        pairs.filter(filter).sortedWith(sort).forEach {
            if (!firstExpected.contains(it.expected.id) && !firstActual.contains(it.actual.id)) {
                siftedExpected.add(it.expected.id)
                siftedActual.add(it.actual.id)
                firstExpected.add(it.expected.id)
                firstActual.add(it.actual.id)
                val diffList = diff.invoke(it)
                if (diffList.isNotEmpty()) failList.add(ComparisonFault(it.expected, it.actual, diffList))
            }
        }
        clear()
    }

    private fun clear() {
        pairs.removeAll { siftedExpected.contains(it.expected.id) && siftedActual.contains(it.actual.id) }
    }


    override fun getFaultList(): List<ComparisonFault<E>> = failList

    class ElementPair<E>(
        val expected: IElement<E>,
        val actual: IElement<E>
    ) {
        private val sizeFault = expected.size.fault(actual.size)
        private val positionFault = expected.position.fault(actual.position)
        val fault = max(sizeFault, positionFault)
        val differenceList = expected.data.compare(actual.data)
        val isDataEqual = differenceList.isEmpty()
        fun equalWithViolation(violation: Int) = isDataEqual && sizeFault <= violation && positionFault <= violation
        fun notEqualDataUnderViolation(violation: Int) =
            !isDataEqual && sizeFault <= violation && positionFault <= violation
    }

}
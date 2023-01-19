package ru.webrelab.layout_inspection_tool.screen_difference

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UtilsKtTest {
    @ParameterizedTest
    @CsvSource(
        value = [
            "Lorem ipsum dolor;true",
            "orem ipsum dolor;false",
            "Lorem ipsum dolo;false",
            "Lorem ipsm dolor;false",
            "Lorem ipsuim dolor;false"
        ],
        delimiter = ';'
    )
    fun stringWithoutMask(actual: String, result: Boolean) {
        val expected = "Lorem ipsum dolor"
        assertEquals(result, equalsWithMask(expected, actual))
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "*Lorem ipsum dolor;Lorem ipsum dolor;true",
            "*orem ipsum dolor;Lorem ipsum dolor;true",
            "* ipsum dolor;Lorem ipsum dolor;true",
            "*n ipsum dolor;Lorem ipsum dolor;false",
            "* ipsum dolor;Lorem ipsum dolor d;false",
        ],
        delimiter = ';'
    )
    fun firstAsterisk(expected: String, actual: String, result: Boolean) {
        assertEquals(result, equalsWithMask(expected, actual))
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "Lorem ipsum dolor*;Lorem ipsum dolor;true",
            "Lorem ipsum dolo*;Lorem ipsum dolor;true",
            "Lorem ipsum *;Lorem ipsum dolor;true",
            "Lorem ipsum f*;Lorem ipsum dolor;false",
            "Lorem ipsum *;LLorem ipsum dolor;false"
        ],
        delimiter = ';'
    )
    fun lastAsterisk(expected: String, actual: String, result: Boolean) {
        assertEquals(result, equalsWithMask(expected, actual))
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "Lorem ip*sum dolor;Lorem ipsum dolor;true",
            "Lorem ip*um dolor;Lorem ipsum dolor;true",
            "Lorem * dolor;Lorem ipsum dolor;true",
            "Lor*or;Lorem ipsum dolor;true",
            "Lorem ip*ssum dolor;Lorem ipsum dolor;false",
            "Lorem*dolor;Lorem ipsum bolor;false"
        ],
        delimiter = ';'
    )
    fun middleAsterisk(expected: String, actual: String, result: Boolean) {
        assertEquals(result, equalsWithMask(expected, actual))
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "Lorem ip*sum d*olor;Lorem ipsum dolor;true",
            "Lorem ip*um do*or;Lorem ipsum dolor;true",
            "Lo*psum *or;Lorem ipsum dolor;true",
            "Lo**or;Lorem ipsum dolor;true",
            "Lo*psum *or;Lorem ipsumd dolor;false",
        ],
        delimiter = ';'
    )
    fun multipleMiddleAsterisk(expected: String, actual: String, result: Boolean) {
        assertEquals(result, equalsWithMask(expected, actual))
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "*Lorem ipsum dolor*;Lorem ipsum dolor;true",
            "*orem ipsum dolo*;Lorem ipsum dolor;true",
            "* ipsum *;Lorem ipsum dolor;true",
            "*Lorem ip sum dolor*;Lorem ipsum dolor;false",
        ],
        delimiter = ';'
    )
    fun doubleSideAsterisk(expected: String, actual: String, result: Boolean) {
        assertEquals(result, equalsWithMask(expected, actual))
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "*Lorem ip*sum dolor*;Lorem ipsum dolor;true",
            "*Lorem ip*sum d*olor*;Lorem ipsum dolor;true",
            "*orem i*sum *olo*;Lorem ipsum dolor;true",
            "* i*s*m *;Lorem ipsum dolor;true",
            "*Lorem *dolor*;Lorem ipsum volor;false",
            "*Lo*lor*;Lorem ipsum door;false"
        ],
        delimiter = ';'
    )
    fun doubleSideAndMiddleAsterisk(expected: String, actual: String, result: Boolean) {
        assertEquals(result, equalsWithMask(expected, actual))
    }
}
package ru.webrelab.layout_inspection_tool.playwright

import com.microsoft.playwright.ElementHandle
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.webrelab.layout_inspection_tool.Executor
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.default_configurations.WebSeleniumConfiguration
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.PlaywrightWebLayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.enums.BrowserScreenSize.HD_READY
import ru.webrelab.layout_inspection_tool.enums.Environment
import ru.webrelab.layout_inspection_tool.enums.WebMeasuringType
import ru.webrelab.layout_inspection_tool.repositories.TestingPiece
import ru.webrelab.layout_inspection_tool.screen_utils.setBrowserViewportSize

class PwLitTest {

    @BeforeEach
    fun before() {
        LitConfig.set(
            WebSeleniumConfiguration(
                "src/jvmTest/resources/data/layouts",
                PlaywrightWebLayoutInspectionBehavior(
                    5,
                    PwAdapter(),
                    {},
                    {}
                )
            )
        )
    }

    @AfterEach
    fun after() {
        PwEnv.page.close()
        PwEnv.browser.close()
    }

    @Test
    fun testLargePage() {
        PwEnv.page.navigate("file://${System.getProperty("user.dir")}/src/jvmTest/resources/data/html/sienna/index.html")


        val testingPieces = mutableListOf<TestingPiece<ElementHandle>>()
        val container = PwEnv.page.querySelector("//body")
        testingPieces.add(
            TestingPiece("All page", container, listOf(WebMeasuringType.DECOR))

        )
        val executor = Executor(
            testingPieces,
            container,
            "full_page",
            Environment.CHROME,
            "sienna",
            "playwright",
            "landing"
        )

        setBrowserViewportSize(HD_READY)
        val t = System.currentTimeMillis()
        executor.execute()
        println("-------------------------------")
        println("Execution time: ${(System.currentTimeMillis() - t)}")
    }
}
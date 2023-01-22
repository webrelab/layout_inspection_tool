package ru.webrelab.layout_inspection_tool.selenium_web_driver

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import ru.webrelab.layout_inspection_tool.Executor
import ru.webrelab.layout_inspection_tool.LitConfig
import ru.webrelab.layout_inspection_tool.default_configurations.WebSeleniumConfiguration
import ru.webrelab.layout_inspection_tool.default_configurations.behaviors.SeleniumWebLayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.enums.BrowserScreenSize
import ru.webrelab.layout_inspection_tool.enums.Environment
import ru.webrelab.layout_inspection_tool.enums.WebMeasuringType
import ru.webrelab.layout_inspection_tool.repositories.TestingPiece
import ru.webrelab.layout_inspection_tool.screen_utils.setBrowserViewportSize

class WdLitTest {

    @BeforeEach
    fun before() {
        LitConfig.set(
            WebSeleniumConfiguration(
                "src/jvmTest/resources/data/layouts",
                SeleniumWebLayoutInspectionBehavior(
                    5,
                    WdAdapter(),
                    {},
                    {}
                )
            )
        )
    }

    @AfterEach
    fun after() {
        WdEnv.driver.close()
        WdEnv.driver.quit()
    }

    @Test
    fun testLargePage() {
        // create snapshot
        WdEnv.driver["file://${System.getProperty("user.dir")}/src/jvmTest/resources/data/html/sienna/index.html"]

        val testingPieces = mutableListOf<TestingPiece<WebElement>>()
        val container = WdEnv.driver.findElement(By.tagName("body"));
        testingPieces.add(
            TestingPiece("All page", container, listOf(WebMeasuringType.ALL))
        )
        val executor = Executor<WebElement> (
            testingPieces,
            container,
            "full_page",
            Environment.CHROME,
            "sienna",
            "webdriver",
            "landing"
        )

        setBrowserViewportSize(BrowserScreenSize.HD_READY)
        executor.execute()
    }
}
package ru.webrelab.layout_inspection_tool.selenium_web_driver

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

object WdEnv {
    val driver: WebDriver
    init {
        System.setProperty("webdriver.chrome.driver", "src/jvmTest/resources/chromedriver_109")
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage")
        chromeOptions.setAcceptInsecureCerts(true)
        driver = ChromeDriver(chromeOptions)
    }
}
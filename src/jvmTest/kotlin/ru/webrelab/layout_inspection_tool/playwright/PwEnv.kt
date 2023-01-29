package ru.webrelab.layout_inspection_tool.playwright

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright

object PwEnv {
    var browser: Browser = Playwright.create().chromium().launch(BrowserType.LaunchOptions().setHeadless(false))
    var page: Page = browser.newPage()
    fun reset() {
        page.close()
        browser.close()
        browser = Playwright.create().chromium().launch(BrowserType.LaunchOptions().setHeadless(false))
        page = browser.newPage()
    }
}
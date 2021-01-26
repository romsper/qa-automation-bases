package main.frontend.helpers

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.WebDriverProvider
import main.backend.helpers.Properties.Companion.properties
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.LocalFileDetector
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL

open class InitDriverProvider : WebDriverProvider {

    init {
        Configuration.baseUrl = "https://vk.com"
        Configuration.browserSize = "1366x768"
        Configuration.timeout = 15000
        Configuration.reopenBrowserOnFail = true

//        Proxy configuration for Selenide
//        Configuration.proxyEnabled = true
//        Configuration.proxyHost =
//        Configuration.fileDownload = FileDownloadMode.PROXY
    }

    var HUB_URL = if (System.getProperty("HUB_URL").isNullOrEmpty()) "localhost" else System.getProperty("HUB_URL")

    override fun createDriver(capabilities: DesiredCapabilities): RemoteWebDriver {
        return when (properties().browserName) {
            "chrome" -> {
                ChromeOptions().apply {
                    setCapability("version", "86")
                    setCapability("enableVNC", true)
                    setCapability("acceptInsecureCerts", true)
                }
            }
            else -> throw Error("Browser is not defined")
        }
            .run { RemoteWebDriver(URL("http://$HUB_URL:4444/wd/hub"), this) }
            .apply { this.fileDetector = LocalFileDetector() }
    }
}
package com.spring.stockAnalyzer.infrastructure.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SeleniumClient {
    private static final Log logger = LogFactory.getLog(SeleniumClient.class);

    @Autowired
    public SeleniumClient() {
        WebDriverManager.chromedriver().setup();
        logger.info("chrome driver set up successfully.");
    }

    public <TResult> TResult triggerSelenium(Function<WebDriver, TResult> action) {
        WebDriver webDriver = getWebDriver();
        try {
            return action.apply(webDriver);
        } catch (Exception ex) {
            throw ex;
        } finally {
            webDriver.close();
        }
    }

    private WebDriver getWebDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--headless");
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        return webDriver;
    }
}

package com.spring.stockAnalyzer.infrastructure.selenium;

import com.evanlennick.retry4j.CallExecutorBuilder;
import com.evanlennick.retry4j.Status;
import com.evanlennick.retry4j.config.RetryConfig;
import com.evanlennick.retry4j.config.RetryConfigBuilder;
import com.spring.stockAnalyzer.application.data.StockData;
import com.spring.stockAnalyzer.core.stock.Stock;
import com.spring.stockAnalyzer.core.stock.StockDataLoader;
import com.spring.stockAnalyzer.core.stockprice.StockPrice;
import com.spring.stockAnalyzer.infrastructure.EnvironmentVariableDecoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.*;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class SeleniumStockDataLoader implements StockDataLoader {
    private static final Log logger = LogFactory.getLog(SeleniumStockDataLoader.class);
    private final EnvironmentVariableDecoder environment;
    private final SeleniumClient seleniumClient;

    public SeleniumStockDataLoader(EnvironmentVariableDecoder environment, SeleniumClient seleniumClient) {
        this.environment = environment;
        this.seleniumClient = seleniumClient;
    }

    @Override
    public Set<Stock> getStocks(String[] categories) {
        return seleniumClient.triggerSelenium( webDriver -> getStocks(webDriver, categories));
    }

    @Override
    public Optional<StockPrice> getStockPrice(String stockId) {
        return seleniumClient.triggerSelenium(webDriver -> {
            StockPrice stockPrice = getStockPrice(stockId, webDriver, true);
            return Optional.of(stockPrice);
        });
    }

    @Override
    public List<StockData> getStockWithPrices(String[] categories) {
        return seleniumClient.triggerSelenium(webDriver -> {
            Set<Stock> stocks = getStocks(webDriver, categories);
            List<StockData> stockDataList = new LinkedList<>();
            List<String> skippedStockIds = new LinkedList<>();
            boolean needAdjustMaOptions = true;
            int index = 0;
            for(Stock stock: stocks) {
                try {
                    logger.info(String.format("%d: 开始读取股票%s价格", ++index, stock.getId()));
                    StockPrice price = getStockPrice(stock.getId(), webDriver, needAdjustMaOptions);
                    stockDataList.add(new StockData(stock, price));
                    needAdjustMaOptions = false;
                }catch(Exception ex) {
                    logger.info(String.format("%d: 读取股票%s价格出现错误,已忽略", index, stock.getId()));
                    skippedStockIds.add(stock.getId());
                }
            }

            logger.info(String.format("完成股票价格读取, 已忽略%d条数据, 他们的股票代码分别是%s", skippedStockIds.size(), skippedStockIds));

            return stockDataList;
        });
    }

    private Set<Stock> getStocks(WebDriver webDriver, String[] categories) {
        String uri = environment.getProperty("app.stock-list-url");
        webDriver.get(uri);
        if(categories.length == 0) {
            categories = environment.getProperty("app.stock-categories").split(",");
        }

        Set<Stock> stocks = new HashSet<>();
        for(String category: categories) {
            stocks.addAll(getStockListByCategory(webDriver, category));
        }

        return stocks;
    }

    private Set<Stock> getStockListByCategory(@NotNull WebDriver webDriver, String category) {
        logger.info(String.format("开始从%s读取股票列表", category));

        WebElement categoryLink = webDriver.findElement(By.xpath(String.format("//div[@id='tab']/ul/li/a[contains(text(), '%s')]", category)));
        String link = categoryLink.getAttribute("href");
        String baseLink = environment.getProperty("app.stock-list-url");
        webDriver.get(link);

        int page = 1;
        logger.info(String.format("开始从%s读取股票列表第%d页", category, page++));
        Set<Stock> stocks = getStockListFromTable(webDriver);

        while( retry(() -> !webDriver.findElement(By.xpath("//a[contains(text(), '下一页')]")).getAttribute("class").contains("disabled"))) {
            retry (() -> {
                webDriver.findElement(By.xpath("//a[contains(text(), '下一页')]")).click();
                return null;
            });
            logger.info(String.format("开始从%s读取股票列表第%d页", category, page++));
            stocks.addAll(getStockListFromTable(webDriver));
        }

        logger.info(String.format("从%s获得%d条数据", category, stocks.size()));

        return stocks;
    }

    private Set<Stock> getStockListFromTable(WebDriver webDriver) {
        return retry(() -> {
            Set<Stock> stocks = new HashSet<>();
            Iterator<WebElement> idElements = webDriver.findElements(By.xpath("//table[@id='table_wrapper-table']/tbody/tr/td[2]/a")).iterator();
            Iterator<WebElement> nameElements = webDriver.findElements(By.xpath("//table[@id='table_wrapper-table']/tbody/tr/td[3]/a")).iterator();
            while(idElements.hasNext() && nameElements.hasNext()) {
                Stock stock = new Stock(idElements.next().getText(), nameElements.next().getText());
                stocks.add(stock);
            }

            return stocks;
        });
    }

    @Nullable
    private StockPrice getStockPrice(String stockId, WebDriver webDriver, boolean needAdjustMaOptions) {
        logger.info(String.format("开始读取股票%s价格", stockId));
        String url = environment.getProperty("app.stock-search-url");
        webDriver.get(url);
        retry(() -> {
            WebElement searchInput = webDriver.findElement(By.xpath("//input[@id='suggest01_input']"));
            searchInput.sendKeys(stockId);
            return null;
        });

        String stockUrl = retry(() -> webDriver.findElement(By.xpath(String.format("//a[contains(text(), '%s')]", stockId))).getAttribute("href"));
        webDriver.get(stockUrl);

        retry(() -> {
            WebElement dayKBtn = webDriver.findElement(By.xpath("//a[contains(text(), '日K')]"));
            dayKBtn.click();
            return null;
        });

        if(needAdjustMaOptions) {
            retry(() -> {
                WebElement ma30Span = webDriver.findElement(By.xpath("//span[contains(text(), 'MA30')]"));
                ma30Span.click();
                return null;
            });

            webDriver.switchTo().frame("sinafinancehtml5indicatorscfgpanel");
            WebElement input1 = webDriver.findElement(By.xpath("//div[@id='mainArea']/div[1]/input[@type='number']"));
            input1.clear();
            input1.sendKeys("20");
            WebElement input2 = webDriver.findElement(By.xpath("//div[@id='mainArea']/div[2]/input[@type='number']"));
            input2.clear();
            input2.sendKeys("30");
            WebElement input3 = webDriver.findElement(By.xpath("//div[@id='mainArea']/div[3]/input[@type='number']"));
            input3.clear();
            input3.sendKeys("60");
            WebElement input4 = webDriver.findElement(By.xpath("//div[@id='mainArea']/div[4]/input[@type='number']"));
            input4.clear();
            input4.sendKeys("120");
            webDriver.findElement(By.xpath("//button[@id='btn_ok']")).click();

            webDriver.switchTo().defaultContent();
        }

        Float ma20 = getMaNumber(webDriver, "MA20");
        Float ma30 = getMaNumber(webDriver, "MA30");
        Float ma60 = getMaNumber(webDriver, "MA60");
        Float ma120 = getMaNumber(webDriver, "MA120");

        String priceStr = webDriver.findElement(By.xpath("//div[@id='price']")).getText().trim();
        Float price = Float.parseFloat(priceStr);

        StockPrice stockPrice = new StockPrice(stockId, price, ma20, ma30, ma60, ma120);

        logger.info(String.format("读取股票%s价格: %f, MA20: %f, MA30: %f, MA60: %f, MA120: %f", stockId, price, ma20, ma30, ma60, ma120));
        return stockPrice;
    }

    private float getMaNumber(WebDriver webDriver, String maLabel) {
        float maNumber = retry(() -> {
            WebElement maSpan = webDriver.findElement(By.xpath(String.format("//span[contains(text(), '%s')]", maLabel)));
            String maNumberStr = maSpan.getText();
            return Float.parseFloat(maNumberStr.split(":")[1].trim());
        });

        return maNumber;
    }

    private WebDriver getWebDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--headless");
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        return webDriver;
    }

    private <TResult> TResult retry(Callable<TResult> callable) {
        RetryConfig config = new RetryConfigBuilder().withMaxNumberOfTries(5)
                .withFixedBackoff().withDelayBetweenTries(2, SECONDS)
                .retryOnSpecificExceptions(RuntimeException.class).build();
        Status<TResult> result = new CallExecutorBuilder().config(config).build().execute(callable);

        return result.getResult();
    }
}

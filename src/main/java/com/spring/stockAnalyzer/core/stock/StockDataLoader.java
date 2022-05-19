package com.spring.stockAnalyzer.core.stock;

import com.spring.stockAnalyzer.application.data.StockData;
import com.spring.stockAnalyzer.core.stockprice.StockPrice;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StockDataLoader {
    Set<Stock> getStocks(String[] categories);

    Optional<StockPrice> getStockPrice(String stockId);

    List<StockData> getStockWithPrices(String[] categories);
}

package com.spring.stockAnalyzer.core.stockprice;

import java.util.List;

public interface StockPriceRepository {
    void save(StockPrice stockPrice);

    void insertBatch(List<StockPrice> stockPrices);
}

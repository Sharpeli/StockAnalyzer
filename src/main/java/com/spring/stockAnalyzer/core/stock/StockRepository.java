package com.spring.stockAnalyzer.core.stock;

import java.util.List;

public interface StockRepository {
    void save(Stock stock);
    void insertBatch(List<Stock> stocks);
}

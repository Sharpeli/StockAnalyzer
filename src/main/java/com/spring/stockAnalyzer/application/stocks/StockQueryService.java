package com.spring.stockAnalyzer.application.stocks;

import com.spring.stockAnalyzer.application.data.StockData;
import com.spring.stockAnalyzer.core.stock.Stock;
import com.spring.stockAnalyzer.core.stock.StockDataLoader;
import com.spring.stockAnalyzer.core.stockprice.StockPrice;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class StockQueryService {
    private StockDataLoader stockDataLoader;
    public Set<Stock> GetStocks(String[] categories) {
        return stockDataLoader.getStocks(categories);
    }
    public Optional<StockPrice> GetStockPrice(String url) {
        return stockDataLoader.getStockPrice(url);
    }
    public List<StockData> GetStockWithPrices(String[] categories) {
        return stockDataLoader.getStockWithPrices(categories);
    }
}

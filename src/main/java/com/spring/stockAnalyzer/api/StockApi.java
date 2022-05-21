package com.spring.stockAnalyzer.api;

import com.spring.stockAnalyzer.api.exception.ResourceNotFoundException;
import com.spring.stockAnalyzer.application.data.StockData;
import com.spring.stockAnalyzer.application.stocks.StockQueryService;
import com.spring.stockAnalyzer.core.stock.Stock;
import com.spring.stockAnalyzer.infrastructure.EnvironmentVariableDecoder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/stocks")
@AllArgsConstructor
public class StockApi {
    private StockQueryService stockQueryService;
    private EnvironmentVariableDecoder environment;

    @GetMapping
    public ResponseEntity<?> stocks(@RequestParam("categories") String[] categories) {
        Set<Stock> stocks = stockQueryService.GetStocks(categories);

        return ResponseEntity.ok(stocks);
    }

    @GetMapping("all")
    public ResponseEntity<?> stocks() {
        String[] categories = environment.getProperty("app.stock-categories").split(",");
        Set<Stock> stocks = stockQueryService.GetStocks(categories);

        return ResponseEntity.ok(stocks);
    }

    @GetMapping(path="price/{id}")
    public ResponseEntity<?> stockPrice(@PathVariable("id") String id) {
        return stockQueryService.GetStockPrice(id)
                .map(price -> {
                    return ResponseEntity.ok(price);
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping(path="prices")
    public ResponseEntity<?> stockPrices(@RequestParam("categories") String[] categories) {
        List<StockData> stocks = stockQueryService.GetStockWithPrices(categories);

        return ResponseEntity.ok(stocks);
    }
}

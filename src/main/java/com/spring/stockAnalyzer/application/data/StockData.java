package com.spring.stockAnalyzer.application.data;

import com.spring.stockAnalyzer.core.stock.Stock;
import com.spring.stockAnalyzer.core.stockprice.StockPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockData {
    private Stock stock;
    private StockPrice price;
}

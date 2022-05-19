package com.spring.stockAnalyzer.core.stockprice;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class StockPrice {
    private UUID id;
    private String stockId;
    private float price;
    private float MA20;
    private float MA30;
    private float MA60;
    private float MA120;
    private UUID syncId;


    public StockPrice(String stockId, float price, float MA20, float MA30, float MA60, float MA120) {
        this.stockId = stockId;
        this.price = price;
        this.MA20 = MA20;
        this.MA30 = MA30;
        this.MA60 = MA60;
        this.MA120 = MA120;
    }
}

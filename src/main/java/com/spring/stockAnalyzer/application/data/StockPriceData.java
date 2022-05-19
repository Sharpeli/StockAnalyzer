package com.spring.stockAnalyzer.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceData {
    private UUID id;
    private String stockId;
    private String stockName;
    private float price;
    private float MA20;
    private float MA30;
    private float MA60;
    private float MA120;
    private UUID syncId;

    public float getMaValue(String maType) {
        switch (maType) {
            case "MA20":
                return MA20;
            case "MA30":
                return MA30;
            case "MA60":
                return MA60;
            case "MA210":
                return MA120;
            default:
                throw new IllegalArgumentException("MA type should be one of \"MA20\", \"MA30\", \"MA60\" and \"MA120\"");
        }
    }
}

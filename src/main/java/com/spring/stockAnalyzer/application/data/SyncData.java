package com.spring.stockAnalyzer.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncData {
    private UUID id;
    private DateTime startTime;
    private DateTime endTime;
    private boolean isSuccessful;
    private List<StockPriceData> stockPrices;
}

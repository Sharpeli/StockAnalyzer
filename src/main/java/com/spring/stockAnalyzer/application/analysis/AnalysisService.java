package com.spring.stockAnalyzer.application.analysis;

import com.spring.stockAnalyzer.application.data.StockPriceData;
import com.spring.stockAnalyzer.application.data.SyncData;
import com.spring.stockAnalyzer.infrastructure.mybatis.readService.SyncReadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnalysisService {
    private SyncReadService syncReadService;
    public List<StockPriceData> getGrowthPotentialStocks(StockAnalysisParam param) {
        List<SyncData> syncDataList;
        if(param.isStrictGrowing()) {
            syncDataList = syncReadService.getSuccessfulSyncsOrderByEndTimeDesc(param.getMinimumGrowthDays());
        } else {
            syncDataList = syncReadService.getSuccessfulSyncsIn2DatesOrderByEndTimeDesc(param.getMinimumGrowthDays());
        }

        if(syncDataList.isEmpty()) {
            throw new RuntimeException("There is no latest sync data");
        }

        List<StockPriceData> latestPriceDataList = syncDataList.get(0).getStockPrices();

        List<StockPriceData> result =  latestPriceDataList.stream().filter(latestPriceData -> {
            String maType = param.getMaType();
            float currentPrice = latestPriceData.getPrice();
            float currentMaValue = latestPriceData.getMaValue(maType);
            if((param.getUpOrBelowMaLine() == "UP" && currentPrice < currentMaValue) ||
                    (param.getUpOrBelowMaLine() == "BELOW" && currentPrice > currentMaValue)) {
                return false;
            }

            if((Math.abs(currentPrice - currentMaValue) / currentMaValue) > (param.getOffsetWithinPercent() / 100)) {
                return false;
            }

            String stockId = latestPriceData.getStockId();
            List<StockPriceData> stockPrices = syncDataList.stream()
                    .map(sync -> sync.getStockPrices().stream()
                            .filter(price -> price.getStockId().equals(stockId)).findFirst().get())
                    .collect(Collectors.toList());

            StockPriceData prev = null;
            for(StockPriceData price: stockPrices) {
                if(prev != null && price.getMaValue(maType) < price.getMaValue(maType)) {
                    return false;
                }
                prev = price;
            }

            return true;
        }).collect(Collectors.toList());

        return result;
    }
}

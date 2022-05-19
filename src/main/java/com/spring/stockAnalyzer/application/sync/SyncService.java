package com.spring.stockAnalyzer.application.sync;

import com.spring.stockAnalyzer.application.data.StockData;
import com.spring.stockAnalyzer.application.data.SyncData;
import com.spring.stockAnalyzer.application.stocks.StockQueryService;
import com.spring.stockAnalyzer.application.stocks.SyncResult;
import com.spring.stockAnalyzer.core.stock.Stock;
import com.spring.stockAnalyzer.core.stock.StockRepository;
import com.spring.stockAnalyzer.core.stockprice.StockPrice;
import com.spring.stockAnalyzer.core.stockprice.StockPriceRepository;
import com.spring.stockAnalyzer.core.syncs.Sync;
import com.spring.stockAnalyzer.core.syncs.SyncRepository;
import com.spring.stockAnalyzer.infrastructure.mybatis.readService.StockReadService;
import com.spring.stockAnalyzer.infrastructure.mybatis.readService.SyncReadService;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SyncService {
    private static final Log logger = LogFactory.getLog(SyncService.class);
    private StockQueryService stockQueryService;
    private StockReadService stockReadService;
    private SyncRepository syncRepository;
    private StockRepository stockRepository;
    private StockPriceRepository stockPriceRepository;
    private SyncReadService syncReadService;

    public SyncResult SyncStock(String[] categories) {
           Sync sync = new Sync(UUID.randomUUID(), new DateTime(), null, false);
           syncRepository.save(sync);

           try {
               List<StockData> latestStocks = stockQueryService.GetStockWithPrices(categories);
               List<Stock> stocksInDb = stockReadService.queryStocks();
               Set<Stock> stocksSet = new HashSet<>(stocksInDb);
               List<Stock> stocksToInsert = latestStocks.stream()
                       .map(s -> s.getStock())
                       .filter(c -> !stocksSet.contains(c))
                       .collect(Collectors.toList());
               stockRepository.insertBatch(stocksToInsert);

               List<StockPrice> stockPrices = latestStocks.stream().map(s -> {
                   StockPrice price = s.getPrice();
                   price.setSyncId(sync.getId());
                   price.setId(UUID.randomUUID());

                   return price;
               }).collect(Collectors.toList());

               stockPriceRepository.insertBatch(stockPrices);
               syncRepository.update(sync.getId(), new DateTime(), true);
               return new SyncResult(true);

           } catch (Exception e) {
               logger.error("Sync stock data job failed", e);
               throw e;
           }
    }

    public SyncData getLatestSuccessfulSync() {
        return syncReadService.getLatestSuccessfulSync();
    }
}

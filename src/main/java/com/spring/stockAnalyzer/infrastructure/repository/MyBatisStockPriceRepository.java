package com.spring.stockAnalyzer.infrastructure.repository;

import com.spring.stockAnalyzer.core.stockprice.StockPrice;
import com.spring.stockAnalyzer.core.stockprice.StockPriceRepository;
import com.spring.stockAnalyzer.infrastructure.mybatis.BatchExecutor;
import com.spring.stockAnalyzer.infrastructure.mybatis.mapper.StockPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MyBatisStockPriceRepository implements StockPriceRepository {
    private final StockPriceMapper mapper;
    private final BatchExecutor batchExecutor;

    @Autowired
    public MyBatisStockPriceRepository(StockPriceMapper mapper, BatchExecutor batchExecutor) {
        this.mapper = mapper;
        this.batchExecutor = batchExecutor;
    }

    @Override
    public void save(StockPrice stockPrice) {
        this.mapper.insert(stockPrice);
    }

    @Override
    public void insertBatch(List<StockPrice> stockPrices) {
        this.batchExecutor.executeInBatch(stockPrices, StockPriceMapper.class, (stockPrice, mapper) -> {
            mapper.insert(stockPrice);
        });
    }
}

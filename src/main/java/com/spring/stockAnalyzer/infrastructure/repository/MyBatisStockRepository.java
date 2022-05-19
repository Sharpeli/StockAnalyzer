package com.spring.stockAnalyzer.infrastructure.repository;

import com.spring.stockAnalyzer.core.stock.Stock;
import com.spring.stockAnalyzer.core.stock.StockRepository;
import com.spring.stockAnalyzer.infrastructure.mybatis.BatchExecutor;
import com.spring.stockAnalyzer.infrastructure.mybatis.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MyBatisStockRepository implements StockRepository {
    private StockMapper mapper;
    private BatchExecutor batchExecutor;

    @Autowired
    public MyBatisStockRepository(StockMapper mapper, BatchExecutor batchExecutor) {
        this.mapper = mapper;
        this.batchExecutor = batchExecutor;
    }

    @Override
    public void save(Stock stock) {
        mapper.insert(stock);
    }

    @Override
    public void insertBatch(List<Stock> stocks) {
        this.batchExecutor.executeInBatch(stocks, StockMapper.class, (stock, mapper) -> {
            mapper.insert(stock);
        });
    }
}

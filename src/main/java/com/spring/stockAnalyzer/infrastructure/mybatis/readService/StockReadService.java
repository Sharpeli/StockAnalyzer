package com.spring.stockAnalyzer.infrastructure.mybatis.readService;

import com.spring.stockAnalyzer.core.stock.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockReadService {
    List<Stock> queryStocks();
}

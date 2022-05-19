package com.spring.stockAnalyzer.infrastructure.mybatis.mapper;

import com.spring.stockAnalyzer.core.stockprice.StockPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StockPriceMapper {
    void insert(@Param("stockPrice")StockPrice stockPrice);
}

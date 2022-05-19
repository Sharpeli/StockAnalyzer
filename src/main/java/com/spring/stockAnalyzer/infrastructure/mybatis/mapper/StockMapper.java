package com.spring.stockAnalyzer.infrastructure.mybatis.mapper;

import com.spring.stockAnalyzer.core.stock.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StockMapper {
    void insert(@Param("stock") Stock stock);
}

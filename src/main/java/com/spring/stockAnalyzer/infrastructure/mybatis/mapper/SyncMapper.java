package com.spring.stockAnalyzer.infrastructure.mybatis.mapper;

import com.spring.stockAnalyzer.core.syncs.Sync;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;

import java.util.UUID;

@Mapper
public interface SyncMapper {
    void insert(@Param("sync") Sync sync);
    void update(@Param("id") UUID id, @Param("endTime") DateTime endTime, @Param("isSuccessful") boolean isSuccessful);
}

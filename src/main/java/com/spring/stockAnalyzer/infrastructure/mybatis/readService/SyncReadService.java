package com.spring.stockAnalyzer.infrastructure.mybatis.readService;

import com.spring.stockAnalyzer.application.data.SyncData;
import org.apache.ibatis.annotations.Mapper;
import org.joda.time.DateTime;

import java.util.List;

@Mapper
public interface SyncReadService {
    SyncData getLatestSuccessfulSync();

    List<SyncData> getSuccessfulSyncsOrderByEndTimeDesc(int minimumGrowthDays);

    List<SyncData> getSuccessfulSyncsIn2DatesOrderByEndTimeDesc(int minimumGrowthDays);
}

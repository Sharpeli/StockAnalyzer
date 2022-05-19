package com.spring.stockAnalyzer.infrastructure.repository;

import com.spring.stockAnalyzer.core.syncs.Sync;
import com.spring.stockAnalyzer.core.syncs.SyncRepository;
import com.spring.stockAnalyzer.infrastructure.mybatis.mapper.SyncMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class MyBatisSyncRepository implements SyncRepository {
    private final SyncMapper mapper;

    @Autowired
    public MyBatisSyncRepository(SyncMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(Sync sync) {
        this.mapper.insert(sync);
    }

    @Override
    public void update(UUID id, DateTime endTime, boolean isSuccessful) {
        this.mapper.update(id, endTime, isSuccessful);
    }
}

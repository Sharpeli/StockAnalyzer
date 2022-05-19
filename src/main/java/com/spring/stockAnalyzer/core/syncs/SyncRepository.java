package com.spring.stockAnalyzer.core.syncs;

import org.joda.time.DateTime;

import java.util.UUID;

public interface SyncRepository {
    void save(Sync sync);
    void update(UUID id, DateTime endTime, boolean isSuccessful);
}

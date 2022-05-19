package com.spring.stockAnalyzer.core.syncs;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Sync {
    private UUID id;
    private DateTime startTime;
    private DateTime endTime;
    private boolean isSuccessful;
}

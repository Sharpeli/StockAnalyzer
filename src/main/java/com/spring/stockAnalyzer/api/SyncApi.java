package com.spring.stockAnalyzer.api;

import com.spring.stockAnalyzer.application.stocks.SyncResult;
import com.spring.stockAnalyzer.application.sync.SyncService;
import com.spring.stockAnalyzer.core.stock.Stock;
import com.spring.stockAnalyzer.infrastructure.EnvironmentVariableDecoder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/sync")
@AllArgsConstructor
public class SyncApi {
    private SyncService syncService;
    private EnvironmentVariableDecoder environment;

    @PostMapping
    public ResponseEntity<?> sync(@RequestBody String[] categories) {
        if(categories.length == 0) {
            categories = environment.getProperty("app.stock-categories").split(",");
        }
        SyncResult res = syncService.SyncStock(categories);

        return ResponseEntity.ok(res);
    }

    @GetMapping("latest")
    public ResponseEntity<?> getLatestSync() {
        return ResponseEntity.ok(syncService.getLatestSuccessfulSync());
    }
}

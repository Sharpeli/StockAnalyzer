package com.spring.stockAnalyzer.api;

import com.spring.stockAnalyzer.application.analysis.AnalysisService;
import com.spring.stockAnalyzer.application.analysis.StockAnalysisParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping(path = "/analyze")
@AllArgsConstructor
public class AnalysisApi {
    private final AnalysisService analysisService;

    @GetMapping("grow")
    public ResponseEntity getGrowthPotentialStocks(@Valid StockAnalysisParam param) {
        if(!Arrays.asList("MA20", "MA30", "MA60", "MA120").contains(param.getMaType().toUpperCase())) {
            throw new IllegalArgumentException("MA type should be one of \"MA20\", \"MA30\", \"MA60\" and \"MA120\"");
        }

        if(!Arrays.asList("UP", "BELOW").contains(param.getUpOrBelowMaLine().toUpperCase())) {
            throw new IllegalArgumentException("UpOrBelowMaLine should be one of \"UP\" and \"BELOW\"");
        }

        return ResponseEntity.ok(analysisService.getGrowthPotentialStocks(param));
    }
}

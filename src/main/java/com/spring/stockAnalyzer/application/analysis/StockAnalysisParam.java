package com.spring.stockAnalyzer.application.analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockAnalysisParam {
    @NotBlank
    private String maType;

    @Min(value = 1)
    private int minimumGrowthDays;
    private boolean isStrictGrowing;

    @NotBlank
    private String upOrBelowMaLine;

    @Min(value = 1)
    @Max(value = 99)
    private int offsetWithinPercent;
}

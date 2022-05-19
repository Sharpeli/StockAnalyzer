package com.spring.stockAnalyzer.core.stock;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Stock {
    private String id;
    private String name;
    private String description;

    public Stock(String id, String name) {
        this.id = id;
        this.name = name;
    }
}

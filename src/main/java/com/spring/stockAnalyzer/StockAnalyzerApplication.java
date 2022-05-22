package com.spring.stockAnalyzer;

import com.logviewer.springboot.LogViewerSpringBootConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({LogViewerSpringBootConfig.class})
public class StockAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockAnalyzerApplication.class, args);
	}

}

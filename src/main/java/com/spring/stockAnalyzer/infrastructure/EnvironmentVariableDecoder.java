package com.spring.stockAnalyzer.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@AllArgsConstructor
public class EnvironmentVariableDecoder {
    private final Environment environment;

    @Nullable
    public String getProperty(String key) {
        String value = environment.getProperty(key);
        if(value != null) {
            byte[] decodedBytes = Base64.getDecoder().decode(value);
            return new String(decodedBytes);
        }

        return null;
    }
}

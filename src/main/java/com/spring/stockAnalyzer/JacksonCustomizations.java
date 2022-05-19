package com.spring.stockAnalyzer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonCustomizations {
    @Bean
    public Module stockAnalyzerModules() {
        return new StockAnalyzerModules();
    }

    public static class StockAnalyzerModules extends SimpleModule {
        public StockAnalyzerModules() {
            addSerializer(DateTime.class, new DateTimeSerializer());
        }
    }

    private static class DateTimeSerializer extends StdSerializer<DateTime> {
        protected DateTimeSerializer() {
            super(DateTime.class);
        }


        @Override
        public void serialize(DateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if(value == null) {
                gen.writeNull();
            } else {
                gen.writeString(ISODateTimeFormat.dateTime().print(value));
            }
        }
    }
}

package com.spring.stockAnalyzer.infrastructure;

import com.evanlennick.retry4j.CallExecutorBuilder;
import com.evanlennick.retry4j.Status;
import com.evanlennick.retry4j.config.RetryConfig;
import com.evanlennick.retry4j.config.RetryConfigBuilder;

import java.util.concurrent.Callable;

import static java.time.temporal.ChronoUnit.SECONDS;

public class RetryUtils {
    public static <TResult> TResult retry5timesWith2SecondsDelay(Callable<TResult> callable) {
        RetryConfig config = new RetryConfigBuilder().withMaxNumberOfTries(5)
                .withFixedBackoff().withDelayBetweenTries(2, SECONDS)
                .retryOnSpecificExceptions(RuntimeException.class).build();
        Status<TResult> result = new CallExecutorBuilder().config(config).build().execute(callable);

        return result.getResult();
    }
}

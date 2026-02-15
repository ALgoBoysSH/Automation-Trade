package com.example.autotrade.strategy;

import com.example.autotrade.dto.Candle;
import com.example.autotrade.entity.StrategyConfig;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class VwapPullbackStrategyTest {

    private final VwapPullbackStrategy strategy = new VwapPullbackStrategy();

    @Test
    void shouldGenerateBuySignalOnVwapPullback() {
        StrategyConfig config = new StrategyConfig();
        config.setSlPoints(2);
        config.setTargetPoints(4);

        List<Candle> candles = List.of(
                c(100, 104, 99, 103, 1000),
                c(103, 106, 102, 105, 1200),
                c(105, 108, 104, 107, 1100),
                c(107, 109, 106, 108, 1000),
                c(108, 109, 106, 108, 1400)
        );

        assertTrue(strategy.evaluate("SBIN", candles, config).isPresent());
    }

    private Candle c(double open, double high, double low, double close, long volume) {
        return Candle.builder()
                .timestamp(LocalDateTime.now())
                .open(BigDecimal.valueOf(open))
                .high(BigDecimal.valueOf(high))
                .low(BigDecimal.valueOf(low))
                .close(BigDecimal.valueOf(close))
                .volume(volume)
                .build();
    }
}

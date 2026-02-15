package com.example.autotrade.service.impl;

import com.example.autotrade.dto.Candle;
import com.example.autotrade.service.MarketDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class KiteWebSocketMarketDataService implements MarketDataService {
    private final Map<String, List<Candle>> candlesBySymbol = new HashMap<>();
    private final Map<String, MutableCandle> currentMinute = new HashMap<>();

    @Override
    public void start() {
        // Hook KiteTicker websocket initialization here.
        log.info("Market data service started (connect KiteTicker in production)");
    }

    @Override
    public void stop() {
        log.info("Market data service stopped");
    }

    @Override
    public List<Candle> getRecentCandles(String symbol) {
        return candlesBySymbol.getOrDefault(symbol, List.of());
    }

    @Override
    public void onTick(String symbol, BigDecimal price, long volume) {
        LocalDateTime currentBucket = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        MutableCandle mutable = currentMinute.get(symbol);

        if (mutable == null || !mutable.timestamp.equals(currentBucket)) {
            if (mutable != null) {
                candlesBySymbol.computeIfAbsent(symbol, key -> new ArrayList<>()).add(mutable.toCandle());
                trim(candlesBySymbol.get(symbol), 120);
            }
            mutable = new MutableCandle(currentBucket, price, price, price, price, volume);
            currentMinute.put(symbol, mutable);
            return;
        }

        mutable.high = mutable.high.max(price);
        mutable.low = mutable.low.min(price);
        mutable.close = price;
        mutable.volume += volume;
    }

    private void trim(List<Candle> candles, int max) {
        while (candles.size() > max) {
            candles.remove(0);
        }
    }

    private static class MutableCandle {
        private final LocalDateTime timestamp;
        private final BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private long volume;

        private MutableCandle(LocalDateTime timestamp, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, long volume) {
            this.timestamp = timestamp;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
        }

        private Candle toCandle() {
            return Candle.builder()
                    .timestamp(timestamp)
                    .open(open)
                    .high(high)
                    .low(low)
                    .close(close)
                    .volume(volume)
                    .build();
        }
    }
}
